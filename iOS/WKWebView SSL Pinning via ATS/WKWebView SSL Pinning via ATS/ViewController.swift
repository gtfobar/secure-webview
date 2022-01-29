//
//  ViewController.swift
//  WKWebView SSL Pinning via ATS
//
//  Created by alexander on 1/29/22.
//

import UIKit
import WebKit

class ViewController: UIViewController, WKUIDelegate, WKNavigationDelegate {
    
    @IBOutlet var pinnedWebView: WKWebView!
    let stringURL = "http://github.com"
    
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
        view = pinnedWebView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        

        if let url = NSURL(string: stringURL) {
            let session = URLSession(configuration: URLSessionConfiguration.ephemeral)
            let task = session.dataTask(with: url as URL, completionHandler: { (data, response, error) -> Void in
                if error != nil {
                    print("error: \(error!.localizedDescription)")
                } else if data != nil {
                    if let str = NSString(data: data!, encoding: String.Encoding.utf8.rawValue) {
                            print(str)
                    } else {
                        print("Unable to convert data to text")
                    }
                }
            })
            task.resume()
        } else {
            print("Unable to create NSURL")
        }

    let myURL = URL(string:stringURL)
    let myRequest = URLRequest(url: myURL!)
    pinnedWebView.load(myRequest)
    }
}
