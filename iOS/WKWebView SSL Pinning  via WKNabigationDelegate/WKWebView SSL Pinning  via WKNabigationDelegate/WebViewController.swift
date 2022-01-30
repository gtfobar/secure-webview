//
//  WebViewController.swift
//  WKWebView_SSLPinning_demo
//
//  Created by alexander on 1/19/22.
//
//

import UIKit
import WebKit

class WebViewController: UIViewController, WKUIDelegate, WKNavigationDelegate {
    
    @IBOutlet var pinnedWebView: WKWebView!
    let host = "github.com"
    
    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        
        let webPreferences = WKPreferences()
        // configuration
        webConfiguration.preferences = webPreferences
        
        let webpagePreferences = WKWebpagePreferences()
        // configuration
        webConfiguration.defaultWebpagePreferences = webpagePreferences

                
        pinnedWebView = WKWebView(
            frame: .zero,
            configuration:  webConfiguration
        )
        pinnedWebView.uiDelegate = self
        pinnedWebView.navigationDelegate = self
        view = pinnedWebView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let myURL = URL(string:"https://github.com")
        let myRequest = URLRequest(url: myURL!)
        pinnedWebView.load(myRequest)
    }
    
    public func webView(_ webView: WKWebView, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        if (challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust) {
            if let serverTrust = challenge.protectionSpace.serverTrust {
                var error: CFError?
                let status = SecTrustEvaluateWithError(serverTrust, &error)
                if (status) {
                    
                    if (challenge.protectionSpace.host != host) {
                        completionHandler(URLSession.AuthChallengeDisposition.useCredential, URLCredential(trust:serverTrust))
                        return
                    }
                    // server certificate
                    if let serverCertificate = SecTrustGetCertificateAtIndex(serverTrust, 0) {
                        let serverCertificateData = SecCertificateCopyData(serverCertificate)
                        let data = CFDataGetBytePtr(serverCertificateData);
                        let size = CFDataGetLength(serverCertificateData);
                        let cert1 = NSData(bytes: data, length: size)
                        
                        // bundled certificate
                        let file_der = Bundle.main.path(forResource: host, ofType: "der")
                        
                        if let file = file_der {
                            if let cert2 = NSData(contentsOfFile: file) {
                                if cert1.isEqual(to: cert2 as Data) {
                                    completionHandler(URLSession.AuthChallengeDisposition.useCredential, URLCredential(trust:serverTrust))
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
