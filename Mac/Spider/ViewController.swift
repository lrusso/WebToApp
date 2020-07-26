//
//  ViewController.swift
//  Spider
//
//  Created by Leonardo Javier Russo on 11/07/2020.
//  Copyright © 2020 Leonardo Javier Russo. All rights reserved.
//

import Cocoa
import WebKit

class ViewController: NSViewController, WKUIDelegate, WKScriptMessageHandler
    {
    var webView: WKWebView!
    let saveFilename: String = "MyFile.state"

    override func loadView()
        {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "readBlob")
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

    func webView(_ webView: WKWebView, runOpenPanelWith parameters: WKOpenPanelParameters, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping ([URL]?) -> Void) {
        webView.evaluateJavaScript("document.body.style.pointerEvents='none';")
        let openPanel = NSOpenPanel()
        openPanel.canChooseFiles = true
        openPanel.begin { (result) in
            if result == NSApplication.ModalResponse.OK {
                if let url = openPanel.url {
                    webView.evaluateJavaScript("document.body.style.pointerEvents='auto';")
                    completionHandler([url])
                }
            } else if result == NSApplication.ModalResponse.cancel {
                webView.evaluateJavaScript("document.body.style.pointerEvents='auto';")
                completionHandler(nil)
            }
        }
    }

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        let fileContent = message.body as! NSString
        let fileFolder = FileManager.default.urls(for: .downloadsDirectory, in: .userDomainMask)[0] as NSURL
        guard let filePath = fileFolder.appendingPathComponent(saveFilename) else { return }
        do {
            try fileContent.write(to: filePath, atomically: true, encoding: String.Encoding.utf8.rawValue)
        }
        catch {
        }
    }

}
