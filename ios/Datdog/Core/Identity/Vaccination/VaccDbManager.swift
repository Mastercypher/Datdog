//
//  VaccDbManager.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import SQLite
import UIKit

class VaccDbManager {
    
    var mDatabase: Connection!
    let tableName = "vaccination"
    let mTable = Table("vaccination")
    
    let mId = Expression<String>("id")
    let mIdDog = Expression<String>("id_nfc_v")
    let mName = Expression<String>("name_v")
    let mDateWhen = Expression<String>("date_when_v")
    let mDateCreate = Expression<String>("date_create_v")
    let mDateUpdate = Expression<String>("date_update_v")
    let mDateCompleted = Expression<String>("date_completed_v")
    let mDelete = Expression<Int>("delete_v")
    
    
    // Creation of db connection
    init(){
        do {
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask,
                                                                appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("Dogs").appendingPathExtension("sqlite3")
            mDatabase = try Connection(fileUrl.path)
            //print("Connected to " + tableName + " db, path: " + fileUrl.path)
        } catch {
            print(error)
        }
        self.createTable()
    }
    
    // Creation of the Vaccination database
    func createTable() {
        // REFERENCES
        let dogDb = DogDbManager()
        let vacc = dogDb.mTable
        let idDog = dogDb.mId
        // CREATE TABLE
        let createTable = mTable.create(ifNotExists: true) { (table) in
            table.column(mId, primaryKey: true)
            table.column(mIdDog)
            table.column(mName)
            table.column(mDateWhen)
            table.column(mDateCreate)
            table.column(mDateUpdate)
            table.column(mDateCompleted)
            table.column(mDelete)
            table.foreignKey(mIdDog, references: vacc, idDog)
        }
        do {
            try mDatabase.run(createTable)
        } catch {
            print(error)
        }
    }
    
    func getAll(dog: Dog) -> Array<Vaccination>? {
        var allVaccs = Array<Vaccination>()
        
        do {
            let query = mTable.filter(mIdDog == dog.mId)
            let vaccs = try mDatabase.prepare(query)
            for vacc in vaccs {
                let curVac = Vaccination(id: vacc[mId], idDog: vacc[mIdDog], name: vacc[mName], dateWhen: vacc[mDateWhen],
                                         dateCreate: vacc[mDateCreate], dateUpdate: vacc[mDateUpdate],
                                         dateCompleted: vacc[mDateCompleted], delete: vacc[mDelete])
                if curVac.mDelete == UtilProj.DBSTATUS.AVAILABLE {
                    allVaccs.append(curVac)
                }
            }
        } catch {
            print(error)
        }
        return allVaccs
    }
    
    func getById(id: String) -> Vaccination?{
        var vaccFound: Vaccination?
        
        do {
            let query = mTable.filter(mId == id)
            let vaccs = try mDatabase.prepare(query)
            for vacc in vaccs {
                // Get only if he's not deleted
                let curVac = Vaccination(id: vacc[mId], idDog: vacc[mIdDog], name: vacc[mName], dateWhen: vacc[mDateWhen],
                                        dateCreate: vacc[mDateCreate], dateUpdate: vacc[mDateUpdate],
                                        dateCompleted: vacc[mDateCompleted], delete: vacc[mDelete])
                if curVac.mDelete == UtilProj.DBSTATUS.AVAILABLE {
                    vaccFound = curVac
                }
                break
            }
        } catch {
            print(error)
        }
        return vaccFound
    }
    
    // Inser a vacc
    func insert(vacc: Vaccination) -> Bool {
        let query = mTable.insert(mId <- vacc.mId, mIdDog <- vacc.mIdDog, mName <- vacc.mName, mDateWhen <- vacc.mDateWhen,
                                  mDateCreate <- vacc.mDateCreate, mDateUpdate <- vacc.mDateUpdate,
                                  mDateCompleted <- vacc.mDateCompleted, mDelete <- vacc.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred vacc: \(vacc.mName!)")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Update a specific vacc
    func update(vacc: Vaccination) -> Bool {
        // VERY IMPORTANT -> UPDATE DATE
        vacc.mDateUpdate = UtilProj.getDateNow()
        
        let dbVacc = mTable.filter(mId == vacc.mId)
        let query = dbVacc.update(mName <- vacc.mName, mDateWhen <- vacc.mDateWhen,
                                  mDateCreate <- vacc.mDateCreate, mDateUpdate <- vacc.mDateUpdate,
                                  mDateCompleted <- vacc.mDateCompleted, mDelete <- vacc.mDelete)
        do {
            try mDatabase.run(query)
            print("Vaccination \(vacc.mName) updated")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Delete the current Vaccination
    func delete(vacc: Vaccination) -> Bool {
        vacc.mDelete = UtilProj.DBSTATUS.DELETE
        return self.update(vacc: vacc)
    }
}
