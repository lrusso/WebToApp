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

    override func loadView()
        {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "webToApp")
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
        let blobMessage = message.body as? NSDictionary
        let filename = blobMessage?.object(forKey: "filename") as? String ?? ""
        let fileContent = blobMessage?.object(forKey: "fileContent") as? String ?? ""
        let fileFolder = FileManager.default.urls(for: .downloadsDirectory, in: .userDomainMask)[0] as NSURL

        let fileNameRAW = filename

        var fileName = ""
        if (fileNameRAW.contains(".")) {
            fileName = String(fileNameRAW[fileNameRAW.startIndex..<fileNameRAW.lastIndex(of: ".")!])
        } else {
            fileName = fileNameRAW
        }

        var fileFormat = ""
        if (fileNameRAW.contains(".")) {
            fileFormat = String(fileNameRAW[fileNameRAW.lastIndex(of: ".")!..<fileNameRAW.endIndex])
        }

        var fileCanBeCreated = false
        var counter = 0

        while (fileCanBeCreated==false) {
            if (counter==0) {
                guard let filePath = fileFolder.appendingPathComponent(fileNameRAW) else { return }
                if (FileManager.default.fileExists(atPath: filePath.path)==false) {
                    fileName = fileName + fileFormat
                    fileCanBeCreated = true
                } else {
                    counter = counter + 1
                }
            } else {
                let newFileName = fileName + "(" + String(counter) + ")" + fileFormat
                guard let filePath = fileFolder.appendingPathComponent(newFileName) else { return }
                if (FileManager.default.fileExists(atPath: filePath.path)==false) {
                    fileName = newFileName
                    fileCanBeCreated = true
                } else {
                    counter = counter + 1
                }
            }
        }

        guard let filePath = fileFolder.appendingPathComponent(fileName) else { return }

        do {
            try fileContent.write(to: filePath, atomically: true, encoding: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        }
        catch {
        }
    }

}
