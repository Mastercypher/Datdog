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
    
    @IBOutlet weak var viewReport: UIView!
    @IBOutlet weak var viewLost: UIView!
    
    var imvReport: UIImageView!
    var imvLost: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        viewReport.layer.shadowColor = UIColor.lightGray.cgColor
        viewReport.layer.shadowOpacity = 15
        viewReport.layer.shadowOffset = CGSize(width: -2, height: 10)
        viewReport.layer.shadowRadius = 10
        
        viewLost.layer.shadowColor = UIColor.lightGray.cgColor
        viewLost.layer.shadowOpacity = 15
        viewLost.layer.shadowOffset = CGSize(width: -2, height: 10)
        viewLost.layer.shadowRadius = 10
        
        // Set the image for the REPORT view
        imvReport = UIImageView(image: UIImage(named: "image_dog_face_resize_ios"))
        
        
        // Set the image for the LOST view
        imvLost = UIImageView(image: UIImage(named: "image_dog_car_resize_ios"))
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        // Set the size for the REPORT view
        imvReport.frame = CGRect(x: 0, y: 0, width: viewReport.frame.size.width, height: viewReport.frame.size.height)
        imvReport.contentMode = UIViewContentMode.scaleAspectFill
        imvReport.clipsToBounds = true
        imvReport.layer.cornerRadius = 10;
        viewReport.addSubview(imvReport)
        viewReport.sendSubview(toBack: imvReport)
        
        // Set the image for the LOST view
        imvLost.frame = CGRect(x: 0, y: 0, width: viewLost.frame.size.width, height: viewLost.frame.size.height)
        imvLost.contentMode = UIViewContentMode.scaleAspectFill
        imvLost.clipsToBounds = true
        imvLost.layer.cornerRadius = 10;
        viewLost.addSubview(imvLost)
        viewLost.sendSubview(toBack: imvLost)
    }
}

