//
//  ViewController.swift
//  Spider
//
//  Created by Leonardo Javier Russo on 21/07/2020.
//  Copyright © 2020 Leonardo Javier Russo. All rights reserved.
//

import UIKit
import WebKit

class ViewController: UIViewController, WKUIDelegate, WKNavigationDelegate, WKScriptMessageHandler
    {
    var webView: WKWebView!

    override func loadView()
        {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "readBlob")
        webConfiguration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webView = WKWebView (frame: CGRect(x:0, y:0, width: 800, height: 600), configuration:webConfiguration)
        webView.uiDelegate = self
        webView.navigationDelegate = self
        view = webView
        }

    override func viewDidLoad() {
    super.viewDidLoad()
    if let url = Bundle.main.url (forResource: "SpiderGame", withExtension: "htm", subdirectory: "www") {
        let path = url.deletingLastPathComponent()
        self.webView.loadFileURL ( url, allowingReadAccessTo: path)
        self.view = webView
        }
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        defer {
            decisionHandler(.allow)
        }
        if (navigationAction.navigationType == .linkActivated) {

            let url = navigationAction.request.url
            let scheme = url?.scheme ?? ""
            let supportedSchemes = ["blob"]

            if (supportedSchemes.contains(scheme)) {
                print("blob url that must be downloaded as a file")
                print(url ?? "")
                return
                }
            else {
                decisionHandler(.allow)
            }

        }
    }

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print(message.body)
    }

}
