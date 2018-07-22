//
//  DogController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class DogController : UIViewController {

    @IBOutlet weak var btnGoMissing: UIButton!
    @IBOutlet weak var btnGoFriends: UIButton!
    @IBOutlet weak var btnGoConnect: UIButton!
    @IBOutlet weak var btnGoVacinations: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        let imageGoMissing = UIImage(named: "icon_wanted")!
        let imageGoFriends = UIImage(named: "icon_add_friend_dog")!
        let imageGoConnect = UIImage(named: "icon_nfc")!
        let imageGoVacinations = UIImage(named: "icon_vaccination")!
        
        btnGoMissing.setImage(imageGoMissing, for: UIControlState.normal)
        btnGoFriends.setImage(imageGoFriends, for: UIControlState.normal)
        btnGoConnect.setImage(imageGoConnect, for: UIControlState.normal)
        btnGoVacinations.setImage(imageGoVacinations, for: UIControlState.normal)

        btnGoMissing.titleEdgeInsets = UIEdgeInsetsMake(10, (-1 * imageGoMissing.size.width + 10) , 0, 0);
        btnGoFriends.titleEdgeInsets = UIEdgeInsetsMake(10, (-1 * imageGoFriends.size.width + 10) , 0, 0);
        btnGoConnect.titleEdgeInsets = UIEdgeInsetsMake(10, (-1 * imageGoConnect.size.width + 10) , 0, 0);
        btnGoVacinations.titleEdgeInsets = UIEdgeInsetsMake(10, (-1 * imageGoVacinations.size.width + 10) , 0, 0);

        let height = btnGoMissing.frame.size.height
        let width = btnGoMissing.frame.size.width

        // prepare dimension for images
        let heightMissing = (height / 10) * 6
        let widthMissing = (width / 10) * 5
        let heightFriends = (height / 10) * 6
        let widthFriends = (width / 10) * 6
        let heightConnect = (height / 10) * 6
        let widthConnect = (width / 10) * 6
        let heightVacc = (height / 10) * 6
        let widthVacc = (width / 10) * 5
        
        btnGoMissing.imageEdgeInsets = UIEdgeInsetsMake(height - heightMissing, width - widthMissing, 10, 10);
        btnGoFriends.imageEdgeInsets =  UIEdgeInsetsMake(height - heightFriends, width - widthFriends, 10, 10);
        btnGoConnect.imageEdgeInsets = UIEdgeInsetsMake(height - heightConnect, width - widthConnect, 10, 10);
        btnGoVacinations.imageEdgeInsets = UIEdgeInsetsMake(height - heightVacc, width - widthVacc, 10, 10);
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
