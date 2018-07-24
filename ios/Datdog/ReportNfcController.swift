//
//  ReportNfcController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 24/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ReportNfcController: UIViewController {
    

    @IBOutlet weak var aciLoad: UIActivityIndicatorView!
    @IBOutlet weak var lblCode: UILabel!
    @IBOutlet weak var swcLocation: UISwitch!
    @IBOutlet weak var swcPhone: UIView!
    @IBOutlet weak var btnContinue: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        aciLoad.startAnimating()
    }
}
