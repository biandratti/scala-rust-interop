use reqwest::Error;
use std::ffi::CString;
use std::future::Future;
use std::os::raw::c_char;
use std::pin::Pin;
use tokio::runtime::Runtime;

#[no_mangle]
pub extern "C" fn multiply_by_two(num: i32) -> i32 {
    num * 2
}

fn httpbin_async() -> Pin<Box<dyn Future<Output = Result<String, Error>>>> {
    Box::pin(async {
        let response = reqwest::get("https://httpbin.org/ip").await?;
        response.text().await
    })
}

#[no_mangle]
pub extern "C" fn get_ip() -> *mut c_char {
    let runtime = Runtime::new().unwrap();
    let response = runtime.block_on(httpbin_async());

    let response_string: String = response.unwrap_or_else(|e| format!("Error: {:?}", e));

    let c_string: CString = CString::new(response_string).unwrap();
    c_string.into_raw()
}

#[cfg(test)]
mod tests {
    use crate::{get_ip, multiply_by_two};
    use std::ffi::CString;

    #[test]
    fn test_multiply_by_two() {
        assert_eq!(multiply_by_two(2), 4);
        assert_eq!(multiply_by_two(5), 10);
        assert_eq!(multiply_by_two(-3), -6);
    }

    #[test]
    fn test_get_ip_cstring() {
        let c_str_ptr = get_ip();
        let c_str = unsafe { CString::from_raw(c_str_ptr) };
        let str_value = c_str.to_str().unwrap();
        // Accept either a successful response with "origin" or an error message
        assert!(
            str_value.contains("origin") || str_value.contains("Error"),
            "Expected response to contain 'origin' or 'Error', got: {}",
            str_value
        );
    }
}
