//
//  ViewController.swift
//  WKContentBlocker_XHR_demo
//
//  Created by alexander on 1/17/22.
//

import UIKit
import WebKit

class ViewController: UIViewController, WKUIDelegate {
    
    var webView: WKWebView!
    
    override func loadView() {               
        webView = WKWebView()
        webView.uiDelegate = self
        view = webView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let path = Bundle.main.path(forResource: "blockRules", ofType: "json")
        var blockRules = ""
        do {
            blockRules = try String(contentsOfFile: path!, encoding: String.Encoding.utf8)
        } catch {
            print("error while loading blockRules")
()        }
        
        WKContentRuleListStore.default().compileContentRuleList(
            forIdentifier: "block_rule_list",
            encodedContentRuleList: blockRules
        ) { (ruleList, error) in
            guard let safeRuleList = ruleList else {
                return
            }
            
            self.webView.configuration.userContentController.add(safeRuleList)
        }
        
        if let indexURL = Bundle.main.url(forResource: "index", withExtension: "html") {
            print(indexURL)
            webView.loadFileURL(indexURL, allowingReadAccessTo: indexURL)
        }
    }
}
