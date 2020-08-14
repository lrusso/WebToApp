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
    @IBOutlet var webView: WKWebView!

    override func loadView()
        {
        super.loadView()
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "webToApp")
        webConfiguration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webView.uiDelegate = self
        webView.navigationDelegate = self
        }

    override func viewDidLoad() {
    super.viewDidLoad()
    if let url = Bundle.main.url (forResource: "SpiderGame", withExtension: "htm", subdirectory: "www") {
        let path = url.deletingLastPathComponent()
        webView.loadFileURL ( url, allowingReadAccessTo: path)
        }
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
