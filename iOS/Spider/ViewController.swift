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

    override func viewDidLoad() {
        super.viewDidLoad()

        // SETTING THAT THE WEBVIEW CONFIGURATION
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "webToApp")
        webConfiguration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webConfiguration.allowsInlineMediaPlayback = true
        webConfiguration.mediaTypesRequiringUserActionForPlayback = []

        // CREATING THE WEBVIEW WITH A BLACK BACKGROUND
        webView = WKWebView(frame: self.view.frame, configuration: webConfiguration)
        webView.isOpaque = true
        webView.backgroundColor = UIColor.black
        webView.scrollView.backgroundColor = UIColor.black
    
        // LOADING THE LOCAL WEB GAME URL
        let htmlFile = Bundle.main.path(forResource: "www/SpiderGame", ofType: "htm")
        let htmlString = try? String(contentsOfFile: htmlFile!, encoding: String.Encoding.utf8)
        webView.loadHTMLString(htmlString!, baseURL: URL(string: "https://www.yourdomain.com"))

        // ADDING THE WEBVIEW TO THE VIEW AND DELEGATING THE EVENTS
        self.view = self.webView!
        webView.uiDelegate = self
        webView.navigationDelegate = self
        
        // FORCING FOCUS IN THE WEBVIEW EVERY 250 MS
        Timer.scheduledTimer(withTimeInterval: 0.25, repeats: true) { [self] (timer) in
            webView.becomeFirstResponder()
            webView.setNeedsFocusUpdate()
            webView.updateFocusIfNeeded()
        }
    }

    @available(iOS 15, *)
    func webView(
        _ webView: WKWebView,
        requestMediaCapturePermissionFor origin: WKSecurityOrigin,
        initiatedByFrame frame: WKFrameInfo,
        type: WKMediaCaptureType,
        decisionHandler: @escaping (WKPermissionDecision) -> Void
    ) {
        decisionHandler(.grant)
    }

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        do {
            let blobMessage = message.body as? NSDictionary
            let filename = blobMessage?.object(forKey: "filename") as? String ?? ""
            let fileContentRAW = blobMessage?.object(forKey: "fileContent") as? String ?? ""
            let fileContent = Data(base64Encoded: fileContentRAW)!

            guard let writePath = NSURL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(filename) else { return }

            try fileContent.write(to: writePath, options: .atomic)
        
            let activityController = UIActivityViewController(activityItems: [writePath], applicationActivities: nil)
            self.present(activityController, animated: true, completion: nil)
        } catch {
        }
    }
}
