//
//  VaccinationsController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class VaccinationsController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    static let SECTION_TODO = 0
    static let SECTION_COMPLETED = 1
    
    @IBOutlet weak var tbvVaccinations: UITableView!
    @IBOutlet weak var btnAddVacc: UIButton!
    
    var mDog: Dog?
    var mVaccsCompleted = Array<Vaccination>()
    var mVaccsTodo = Array<Vaccination>()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnAddVacc.layer.cornerRadius = 0.5 * btnAddVacc.bounds.size.width;
        btnAddVacc.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mVaccsCompleted.removeAll()
        mVaccsTodo.removeAll()
        // LOAD DATA
        if let dog = mDog {
            let vaccs = VaccDbManager().getAll(dog: dog)
            if let rVaccs = vaccs {
                for vacc in rVaccs {
                    if(vacc.mDateCompleted != "") {
                        mVaccsCompleted.append(vacc)
                    } else {
                        mVaccsTodo.append(vacc)
                    }
                }
            }
        }
        tbvVaccinations.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if(section == VaccinationsController.SECTION_TODO ){
            return mVaccsTodo.count
        } else if(section == VaccinationsController.SECTION_COMPLETED ){
            return mVaccsCompleted.count
        }
        return 0
    }
    
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(section == VaccinationsController.SECTION_TODO ){
            return "Vaccinations todo: \(mVaccsTodo.count)"
            
        } else if(section == VaccinationsController.SECTION_COMPLETED ){
            return "Vaccinations Completed: \(mVaccsCompleted.count)"
        }
        return ""
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "vaccinationCell", for: indexPath)
        switch (indexPath.section){
        case VaccinationsController.SECTION_TODO:
            let vacc = mVaccsTodo[indexPath.row]
            cell.textLabel?.text = vacc.mName
            cell.detailTextLabel?.text = vacc.mDateWhen
            break
        case VaccinationsController.SECTION_COMPLETED:
            let vacc = mVaccsCompleted[indexPath.row]
            cell.textLabel?.text = vacc.mName
            cell.detailTextLabel?.text = vacc.mDateWhen
            break
        default:
            cell.textLabel?.text = "None"
            cell.detailTextLabel?.text = "No data"
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "toAddVacc":
            let destination = segue.destination as! AddVaccinationController
            destination.mDog = mDog
            
        case "toViewVacc":
            self.sendInstanceToView(segue: segue)
            
        default: break
        }
    }
    
    func sendInstanceToView(segue: UIStoryboardSegue){
        let selectedIndex = tbvVaccinations.indexPathForSelectedRow!
        let destination = segue.destination as! ViewVaccinationController
        var vacc: Vaccination?
        switch (selectedIndex.section){
        case VaccinationsController.SECTION_TODO:
            vacc = mVaccsTodo[selectedIndex.row]
            break
        case VaccinationsController.SECTION_COMPLETED:
            vacc = mVaccsCompleted[selectedIndex.row]
            break
        default:
            vacc = nil
        }
        destination.mVacc = vacc
    }
}
