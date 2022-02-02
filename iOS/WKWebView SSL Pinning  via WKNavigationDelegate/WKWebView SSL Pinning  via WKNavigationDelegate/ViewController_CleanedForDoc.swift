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
        
        // Объект challenge.protectionSpace содержит дополнительную информацию о текущей аутентификации.
        // Поле challenge.protectionSpace.authenticationMethod определяет тип аутентификации.
        // Нас интересует аутентификация типа NSURLAuthenticationMethodServerTrust - проверка сертификата сервера.
        if (challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust) {
            
            // Поле challenge.protectionSpace.serverTrust содержит информацию о состоянии текущей SSL-транзакции
            if let serverTrust = challenge.protectionSpace.serverTrust {
                
                // Метод SecTrustGetCertificateAtIndex(...) возвращает сертификат из цепочки сертификатов сервера с заданным индексом.
                if let serverCertificate = SecTrustGetCertificateAtIndex(serverTrust, 0) {
                    let serverCertificateData = SecCertificateCopyData(serverCertificate)
                    let data = CFDataGetBytePtr(serverCertificateData);
                    let size = CFDataGetLength(serverCertificateData);
                    let cert1 = NSData(bytes: data, length: size)
                    
                    // Извлекаем предустановленный в App Bundle приложения сертификат из файла "cert.der"
                    let file_der = Bundle.main.path(forResource: "cert", ofType: "der")
                    if let file = file_der {
                        if let cert2 = NSData(contentsOfFile: file) {
                            if cert1.isEqual(to: cert2 as Data) {
                                
                                // Вызов метод completionHandler(...) оповещает систему о том, что сертификат прошел проверку.
                                // Вызываем его, если сертификат сервера совпадает с сертификатом в "cert.der"
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

