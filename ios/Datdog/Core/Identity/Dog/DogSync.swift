//
//  DogSync.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import UIKit

class DogSync {
    
    func sync(idUser: Int){
        self.downloadData(idUser: idUser)
    }
    
    // Permit to oush the registration data online.
    func downloadData(idUser: Int){
        let this = self
        let urlString = "http://datdog.altervista.org/dog.php?action=select&id_user_d=\(idUser)"
        let url = URL(string: urlString)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: request) { [unowned self](data, response, error) in
            
            if (data != nil) {
                let string = String(data: data!, encoding: .utf8)
                debugPrint(string!)
            }
            
            
            do {
                var remoteDogs = Array<Dog>()
                let respond = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.allowFragments)
                let dictionary = respond as! [String : Any]
                
                let success = dictionary["success"] as? Bool
                let dogsDic = dictionary["additional"] as? [[String: String]]
                let dicCount = dogsDic?.count
                
                if (success! && dicCount != nil && dicCount! > 0) {
                    for dog in dogsDic! {
                        let curDog = Dog(id: dog["id"]!, idNfc: dog["id_nfc_d"]!, idUser: Int(dog["id_user_d"]!)!, name: dog["name_d"]!,
                                         breed: dog["breed_d"]!, colour: dog["colour_d"]!, birth: dog["birth_d"]!, size: Int(dog["size_d"]!)!,
                                         sex: Int(dog["sex_d"]!)!, dateCreate: dog["date_create_d"]!, dateUpdate: dog["date_update_d"]!,
                                         delete: Int(dog["delete_d"]!)!)
                        remoteDogs.append(curDog)
                    }
                    
                }
                // Compare the remote with local
                DispatchQueue.main.async(execute: {
                    this.compareDogs(remoteDogs: remoteDogs)
                })
            } catch {
                debugPrint()
            }
        }
        task.resume()
    }
    
    func compareDogs(remoteDogs: Array<Dog>?) {
        let user = UserDbManager().getCurrent(view: nil)
        let localDogs = DogDbManager().getAll(user: user!)!
        var toRemoteInsert = Array<Dog>()
        var toRemoteUpdate = Array<Dog>()
        var toLocal = Array<Dog>()
        
        for remote in remoteDogs! {
            if localDogs.contains(remote) {
                for local in localDogs {
                    if remote.mId == local.mId {
                        let remoteD = UtilProj.transformToDate(dateString: remote.mDateUpdate)
                        let localD  = UtilProj.transformToDate(dateString: local.mDateUpdate)
                        if remoteD > localD {
                            toLocal.append(remote)
                        } else if remoteD < localD {
                            toRemoteUpdate.append(remote)
                        }
                    }
                }
            } else {
                toLocal.append(remote)
            }
        }
        
        // Inverse, to remote
        for local in localDogs {
            if !remoteDogs!.contains(local) {
                toRemoteInsert.append(local)
            }
        }
        
        self.uploadToLocal(localDogs: toLocal)
        self.uploadToRemote(remoteDogs: toRemoteInsert, update: false)
        self.uploadToRemote(remoteDogs: toRemoteUpdate, update: true)
    }
    
    func uploadToLocal(localDogs: Array<Dog>!) {
        let db = DogDbManager()
        for dog in localDogs {
            if(!db.insert(dog: dog)){
                if(!db.update(dog: dog)){
                    print("Problem remote sync with dog \(dog.mId)")
                }
            }
        }
    }
    
    func uploadToRemote(remoteDogs: Array<Dog>!, update: Bool) {
        var urlString = ""
        for dog in remoteDogs!{
            if(update){
                urlString = "http://datdog.altervista.org/dog.php?action=update&id=\(dog.mId!)&id_nfc_d=\(dog.mIdNfc! ?? "")&id_user_d=\(dog.mIdUser!)&name_d=\(dog.mName!)&breed_d=\(dog.mBreed!)&colour_d=\(dog.mColour!)&birth_d=\(dog.mBirth!)&size_d=\(dog.mSize!)&sex_d=\(dog.mSex!)&date_create_d=\(dog.mDateCreate!)&date_update_d=\(dog.mDateUpdate!)&delete_d=\(dog.mDelete!)"
            } else {
                urlString = "http://datdog.altervista.org/dog.php?action=insert&id=\(dog.mId!)&id_nfc_d=\(dog.mIdNfc!)!&id_user_d=\(dog.mIdUser!)&name_d=\(dog.mName!)&breed_d=\(dog.mBreed!)&colour_d=\(dog.mColour!)&birth_d=\(dog.mBirth!)&size_d=\(dog.mSize!)&sex_d=\(dog.mSex!)&date_create_d=\(dog.mDateCreate!)&date_update_d=\(dog.mDateUpdate!)&delete_d=\(dog.mDelete!)"
            }
            upload(urlString: urlString)
        }
    }
    
    func upload(urlString: String!) {
        let url = URL(string: urlString)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: request) { [unowned self](data, response, error) in
            
            if (data != nil) {
                let string = String(data: data!, encoding: .utf8)
                debugPrint(string!)
            }
            
            do {
                let respond = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.allowFragments)
                let dictionary = respond as! [String : Any]
                let success = dictionary["success"] as? Bool
                
                if (success!) {
                    print("Success sync")

                } else {
                    print("Problem remote sync dog ")
                }
            } catch {
                debugPrint()
            }
        }
        task.resume()
    }
    
}
