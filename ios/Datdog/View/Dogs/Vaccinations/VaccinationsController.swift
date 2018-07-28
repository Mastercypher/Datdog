//
//  VaccinationsController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class VaccinationsController: UIViewController {
    
    @IBOutlet weak var tbvVaccinations: UITableView!
    
    var mDog: Dog?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        // LOAD DATA
        let user = UserDbManager().getCurrent(view: self)
        //mDogs = DogDbManager().getAll(user: user!)
        tbvVaccinations.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        /*if (mDogs != nil) {
            return (mDogs?.count)!
        } else {
            return 0
        }*/
        return 0
    }
    
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        /*
        if mDogs != nil {
            return "You have \(mDogs?.count ?? 0) \((mDogs?.count == 1 ? "dog" : "dogs"))"
        } else{
            return "You have no dogs"
        }
 */
        return ""
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "vaccinationCell", for: indexPath)
        /*let dog = mDogs?[indexPath.row]
        cell.textLabel?.text = dog?.mName
        cell.detailTextLabel?.text = dog?.mColour*/
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        /*
        let selectedIndex = tbvDogs.indexPathForSelectedRow!
        let destinationController = segue.destination as! ViewDogController
        
        destinationController.mDog = mDogs?[selectedIndex.row]
 */
    }
}
