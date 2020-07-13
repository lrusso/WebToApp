//
//  WindowController.swift
//  Spider
//
//  Created by Leonardo Javier Russo on 12/07/2020.
//  Copyright © 2020 Leonardo Javier Russo. All rights reserved.
//

import Foundation
import Cocoa
import WebKit

class WindowController: NSWindowController {

  override func windowDidLoad() {
      super.windowDidLoad()
      //---------------------------------------------------------------------
      // Workaround for a bug in autosave default for window frame:
      self.windowFrameAutosaveName = NSWindow.FrameAutosaveName("MainWindow")
      // It has to be "position" here in code, and no value in storyboard.
      //---------------------------------------------------------------------
  }
}
