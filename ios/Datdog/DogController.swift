//
//  DogController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class DogController : UIViewController {
    //[button setImageEdgeInsets:UIEdgeInsetsMake(0, -70, 0, 0)];

    @IBOutlet weak var btnGoMissing: UIButton!
    @IBOutlet weak var btnGoFriends: UIButton!
    @IBOutlet weak var btnGoConnect: UIButton!
    @IBOutlet weak var btnGoVacinations: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        print(-1 * (btnGoFriends.imageView?.frame.size.width)!)
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnGoFriends.titleEdgeInsets = UIEdgeInsetsMake(0, -1 * (btnGoFriends.imageView?.frame.size.width)!, 0, (btnGoFriends.imageView?.frame.size.width)!);
        
        btnGoFriends.imageEdgeInsets = UIEdgeInsetsMake(0, (btnGoFriends.titleLabel?.frame.size.width)!, 0, -1 * (btnGoFriends.titleLabel?.frame.size.width)!);

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
