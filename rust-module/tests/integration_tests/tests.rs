#[cfg(test)]
mod tests {
    use super::*;
    use std::ffi::CString;
    use std::os::raw::c_char;

    #[test]
    fn test_multiply_by_two() {
        assert_eq!(multiply_by_two(2), 4);
        assert_eq!(multiply_by_two(5), 10);
        assert_eq!(multiply_by_two(-3), -6);
    }

    #[test]
    fn test_get_ip() {
        // Here we mock the async call to httpbin.org/ip
        // and ensure that the response is correctly handled.
        // Note: This is just a basic example of how to test async functions.
        // For real-world scenarios, consider using more robust testing frameworks
        // like `tokio` or `async-std` with mocking libraries.
        let response = httpbin_async();
        let response_string = match response.await {
            Ok(text) => text,
            Err(e) => format!("Error: {:?}", e),
        };

        // Check that the response contains the expected format
        assert!(response_string.contains("origin"));

        // You can add more specific assertions based on the expected behavior of your function
    }

    #[test]
    fn test_get_ip_cstring() {
        // Here we test the get_ip function by checking the result of the returned CString
        let c_str_ptr = get_ip();
        let c_str = unsafe { CString::from_raw(c_str_ptr) };
        let str_value = c_str.to_str().unwrap();

        // Check that the returned string contains the expected format
        assert!(str_value.contains("origin"));

        // You can add more specific assertions based on the expected behavior of your function
    }
}