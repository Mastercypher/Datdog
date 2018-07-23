//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class ReportController: UIViewController {
    
    @IBOutlet weak var btnAddReport: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnAddReport.layer.cornerRadius = 0.5 * btnAddReport.bounds.size.width;
        btnAddReport.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

