SSL pinning in WKWebView via ATS
================================

# Description

This application demonstrates that SSL pinning via `NSPinnedDomains` attribute is not working for `WKWebView`.

SSL pinning is configured for `github.com` in `Info.plist`. For additional details see [NSAppTransportSecurity](https://developer.apple.com/documentation/bundleresources/information_property_list/nsapptransportsecurity).

First it loads `https://github.com` using `NSUrlDomains` and dumps the response to console. Then it loads `github.com` into `WKWebView`.

# Usage

1. Launch it from Xcode.
2. Ensure that both `NSUrlSession` and `WKWebView` succeded in loading `github.com`.
3. Change value of `SPKI-SHA256-BASE64` attribute in `Info.plist`.
4. Launch it again.
5. See that NSUrlSession throws some kind SSL error, while WKWebView loads the page successfully.

