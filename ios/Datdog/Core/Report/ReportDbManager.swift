//
//  ReportDbManager.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import SQLite
import UIKit

class ReportDbManager {
    
    
    var mDatabase: Connection!
    let tableName = "report"
    let mTable = Table("report")
    
    let mId = Expression<String>("id")
    let mIdUser = Expression<Int>("id_user_r")
    let mIdDog = Expression<String>("id_dog_r")
    let mLocation = Expression<String>("location_r")
    let mDateCreate = Expression<String>("date_create_u")
    let mDateUpdate = Expression<String>("date_update_u")
    let mDateFound = Expression<String>("date_found_r")
    let mDelete = Expression<Int>("delete_u")
    
    
    // Creation of db connection
    init(){
        do {
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("users").appendingPathExtension("sqlite3")
            mDatabase = try Connection(fileUrl.path)
            //print("Connected to " + tableName + " db, path: " + fileUrl.path)
        } catch {
            print(error)
        }
        self.createTable()
    }
    
    // Creation of the user database
    func createTable() {
        // REFERENCES
        let userDb = UserDbManager()
        let user = userDb.mTable
        let idUser = userDb.mId
        // ____
        let dogDb = DogDbManager()
        let dog = dogDb.mTable
        let idDog = dogDb.mId
        // CREATE TABLE
        let createTable = mTable.create(ifNotExists: true) { (table) in
            table.column(mId, primaryKey: true)
            table.column(mIdUser)
            table.column(mIdDog)
            table.column(mLocation)
            table.column(mDateCreate)
            table.column(mDateUpdate)
            table.column(mDateFound)
            table.column(mDelete)
            table.foreignKey(mIdUser, references: user, idUser)
            table.foreignKey(mIdDog, references: dog, idDog)
            // FOREIGN KEY("user_id") REFERENCES "users"("id") ON DELETE SET NULL
        }
        do {
            try mDatabase.run(createTable)
        } catch {
            print(error)
        }
    }
    /*
    func getAll(user: User) -> Array<Dog>? {
        var allDogs = Array<Dog>()
        
        do {
            let query = mTable.filter(mIdUser == user.mId)
            let dogs = try mDatabase.prepare(query)
            for dog in dogs {
                let curDog = Dog(id: dog[mId], idNfc: dog[mIdNfc], idUser: dog[mIdUser], name: dog[mName],
                                 breed: dog[mBreed], colour: dog[mColour], birth: dog[mBirth],
                                 size: dog[mSize], sex: dog[mSex], dateCreate: dog[mDateCreate],
                                 dateUpdate: dog[mDateUpdate], delete: dog[mDelete])
                allDogs.append(curDog)
            }
        } catch {
            print(error)
        }
        return allDogs
    }
    
    func getById(id: String) -> Dog?{
        var dogFound: Dog?
        
        do {
            let query = mTable.filter(mId == id)
            let dogs = try mDatabase.prepare(query)
            for dog in dogs {
                // Get only if he's not deleted
                if dog[mDelete] == UtilProj.DBSTATUS.AVAILABLE {
                    dogFound = Dog(id: dog[mId], idNfc: dog[mIdNfc], idUser: dog[mIdUser], name: dog[mName],
                                   breed: dog[mBreed], colour: dog[mColour], birth: dog[mBirth],
                                   size: dog[mSize], sex: dog[mSex], dateCreate: dog[mDateCreate],
                                   dateUpdate: dog[mDateUpdate], delete: dog[mDelete])
                }
                break
            }
        } catch {
            print(error)
        }
        return dogFound
    }
    
    // Inser a dog
    func insert(dog: Dog) -> Bool {
        let query = mTable.insert(mId <- dog.mId, mIdNfc <- dog.mIdNfc, mIdUser <- dog.mIdUser, mName <- dog.mName,
                                  mBreed <- dog.mBreed, mColour <- dog.mColour, mBirth <- dog.mBirth, mSize <- dog.mSize,
                                  mSex <- dog.mSex, mDateCreate <- dog.mDateCreate, mDateUpdate <- dog.mDateUpdate,
                                  mDelete <- dog.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred dog: \(dog.mName!)")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Update a specific dog
    func update(dog: Dog) -> Bool {
        // VERY IMPORTANT -> UPDATE DATE
        dog.mDateUpdate = UtilProj.getDateNow()
        
        let dbDog = mTable.filter(mId == dog.mId)
        let query = dbDog.update(mIdNfc <- dog.mIdNfc, mIdUser <- dog.mIdUser, mName <- dog.mName,
                                 mBreed <- dog.mBreed, mColour <- dog.mColour, mBirth <- dog.mBirth, mSize <- dog.mSize,
                                 mSex <- dog.mSex, mDateCreate <- dog.mDateCreate, mDateUpdate <- dog.mDateUpdate,
                                 mDelete <- dog.mDelete)
        do {
            try mDatabase.run(query)
            print("Dog \(dog.mName) updated")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Delete the current user
    func delete(dog: Dog) -> Bool {
        
        let delete = UtilProj.DBSTATUS.DELETE
        let dbDog = mTable.filter(mId == dog.mId)
        let query = dbDog.update(mId <- dog.mId, mIdNfc <- dog.mIdNfc, mIdUser <- dog.mIdUser, mName <- dog.mName,
                                 mBreed <- dog.mBreed, mColour <- dog.mColour, mBirth <- dog.mBirth, mSize <- dog.mSize,
                                 mSex <- dog.mSex, mDateCreate <- dog.mDateCreate, mDateUpdate <- dog.mDateUpdate,
                                 mDelete <- delete)
        do {
            try mDatabase.run(query)
            print("User \(dog.mName!) deleted")
            return true
        } catch {
            print(error)
            return false
        }
        
    }
 */
}
