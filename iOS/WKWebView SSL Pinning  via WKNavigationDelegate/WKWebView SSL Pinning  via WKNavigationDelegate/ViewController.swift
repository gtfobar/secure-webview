//
//  ViewController.swift
//  WKWebView SSL Pinning  via WKNavigationDelegate
//
//  Created by alexander on 1/29/22.
//

import UIKit
import WebKit

class ViewController: UIViewController, WKUIDelegate, WKNavigationDelegate {
    
    @IBOutlet  var pinnedWebView: WKWebView!
    let host = "github.com"
    let url = "https://github.com"

    override func loadView() {
        pinnedWebView = WKWebView()
        pinnedWebView.uiDelegate = self
        view = pinnedWebView
    }
    
    public func webView(_ webView: WKWebView, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        if (challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust) {
            if let serverTrust = challenge.protectionSpace.serverTrust {
                if (challenge.protectionSpace.host != host) {
                    let status = SecTrustEvaluateWithError(serverTrust, nil)
                    if (true == status) {
                        completionHandler(URLSession.AuthChallengeDisposition.useCredential, URLCredential(trust:serverTrust))
                        return
                    }
                }
                //server certificate
                if let serverCertificate = SecTrustGetCertificateAtIndex(serverTrust, 0) {
                    let serverCertificateData = SecCertificateCopyData(serverCertificate)
                    let data = CFDataGetBytePtr(serverCertificateData);
                    let size = CFDataGetLength(serverCertificateData);
                    let cert1 = NSData(bytes: data, length: size)
                    //bundled certificate
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

    override func viewDidLoad() {
        super.viewDidLoad()
        pinnedWebView.navigationDelegate = self
        let myURL = URL(string: url)
        let myRequest = URLRequest(url: myURL!)
        pinnedWebView.load(myRequest)
        // Do any additional setup after loading the view.
    }


}

