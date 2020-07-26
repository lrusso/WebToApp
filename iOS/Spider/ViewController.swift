//
//  ViewController.swift
//  Spider
//
//  Created by Leonardo Javier Russo on 21/07/2020.
//  Copyright © 2020 Leonardo Javier Russo. All rights reserved.
//

import UIKit
import WebKit

class ViewController: UIViewController, WKUIDelegate, UIBarPositioningDelegate
    {
    var webView: WKWebView!

    override func loadView()
        {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webView = WKWebView (frame: CGRect(x:0, y:0, width: 800, height: 600), configuration:webConfiguration)
        webView.uiDelegate = self
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
    
}
