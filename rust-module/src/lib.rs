use std::ffi::CString;
use std::future::Future;
use std::os::raw::c_char;
use std::pin::Pin;
use tokio::runtime::Runtime;

#[no_mangle]
pub extern "C" fn multiply_by_two(num: i32) -> i32 {
    num * 2
}

fn httpbin_async() -> Pin<Box<dyn Future<Output = Result<String, reqwest::Error>>>> {
    Box::pin(async {
        let response = reqwest::get("https://httpbin.org/ip").await?;
        Ok(response.text().await?)
    })
}

#[no_mangle]
pub extern "C" fn get_ip() -> *mut c_char {
    let runtime = Runtime::new().unwrap();
    let response = runtime.block_on(httpbin_async());

    let response_string: String = match response {
        Ok(text) => text,
        Err(e) => format!("Error: {:?}", e),
    };

    let c_string: CString = CString::new(response_string).unwrap();
    c_string.into_raw()
}
