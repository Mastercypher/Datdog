//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class MissingController: UIViewController {
    
    @IBOutlet weak var btnMissed: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnMissed.layer.cornerRadius = 0.5 * btnMissed.bounds.size.width;
        btnMissed.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

