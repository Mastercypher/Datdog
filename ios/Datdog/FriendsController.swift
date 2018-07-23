//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class FriendsController: UIViewController {
    
    @IBOutlet weak var btnAddFriend: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnAddFriend.layer.cornerRadius = 0.5 * btnAddFriend.bounds.size.width;
        btnAddFriend.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

