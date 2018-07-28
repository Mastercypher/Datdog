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
    
    // Creation of the report database
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
    
    // For section REPORTS
    func getAllReports(user: User) -> Array<Report>? {
        var allReports = Array<Report>()
        
        do {
            // Filter by id of the user
            let query = mTable.filter(mIdUser == user.mId)
            let reports = try mDatabase.prepare(query)
            for report in reports {
                let curReport = Report(id: report[mId], idUser: report[mIdUser], idDog: report[mIdDog], location: report[mLocation],
                                       dateCreate: report[mDateCreate], dateUpdate: report[mDateUpdate], dateFound: report[mDateFound],
                                       delete: report[mDelete])
                allReports.append(curReport)
            }
        } catch {
            print(error)
        }
        return allReports
    }
    
    // For section LOSTS
    func getAllReportLosts(user: User) -> Array<Report>? {
        var allReports = Array<Report>()
        // Get all dog of a user
        let dogs = DogDbManager().getAll(user: user)
        
        do {
            for dog in dogs! {
                // Filter by id of the dog
                let query = mTable.filter(mIdDog == dog.mId)
                let reports = try mDatabase.prepare(query)
                for report in reports {
                    let curReport = Report(id: report[mId], idUser: report[mIdUser], idDog: report[mIdDog], location: report[mLocation],
                                           dateCreate: report[mDateCreate], dateUpdate: report[mDateUpdate], dateFound: report[mDateFound],
                                           delete: report[mDelete])
                    allReports.append(curReport)
                }
            }
        } catch {
            print(error)
        }
        return allReports
    }
    
    func getById(id: String) -> Report?{
        var reportFound: Report?
        
        do {
            let query = mTable.filter(mId == id)
            let reports = try mDatabase.prepare(query)
            for report in reports {
                // Get only if he's not deleted
                if report[mDelete] == UtilProj.DBSTATUS.AVAILABLE {
                    reportFound = Report(id: report[mId], idUser: report[mIdUser], idDog: report[mIdDog], location: report[mLocation],
                                                    dateCreate: report[mDateCreate], dateUpdate: report[mDateUpdate],
                                                    dateFound: report[mDateFound], delete: report[mDelete])
                }
                break
            }
        } catch {
            print(error)
        }
        return reportFound
    }
    
    // Inser a report
    func insert(report: Report) -> Bool {
        let query = mTable.insert(mId <- report.mId, mIdUser <- report.mIdUser, mIdDog <- report.mIdDog, mLocation <- report.mLocation,
                                  mDateCreate <- report.mDateCreate, mDateUpdate <- report.mDateUpdate,
                                  mDateFound <- report.mDateFound, mDelete <- report.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred report: \(report.mId!)")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Update a specific report
    func update(report: Report) -> Bool {
        // VERY IMPORTANT -> UPDATE DATE
        report.mDateUpdate = UtilProj.getDateNow()
        
        let dbReport = mTable.filter(mId == report.mId)
        let query = dbReport.update(mId <- report.mId, mIdUser <- report.mIdUser, mIdDog <- report.mIdDog, mLocation <- report.mLocation,
                                 mDateCreate <- report.mDateCreate, mDateUpdate <- report.mDateUpdate,
                                 mDateFound <- report.mDateFound, mDelete <- report.mDelete)
        do {
            try mDatabase.run(query)
            print("Dog \(report.mId) updated")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Delete the current report
    func delete(report: Report) -> Bool {
        report.mDelete = UtilProj.DBSTATUS.DELETE
        return self.update(report:report)
    }
 
}
