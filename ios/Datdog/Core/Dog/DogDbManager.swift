//
//  DogDbManager.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//
import Foundation
import SQLite
import UIKit

class DogDbManager {
    
    struct STATUS {
        static let CURRENT = 1
        static let NOT_CURRENT = 0
        static let DELETED = 1
        static let ACTIVE = 0
    }
    
    var mDatabase: Connection!
    let tableName = "dog"
    let mTable = Table("dog")
    
    let mId = Expression<String>("id")
    let mIdNfc = Expression<String>("id_nfc_d")
    let mIdUser = Expression<Int>("id_user_d")
    let mName = Expression<String>("name_d")
    let mBreed = Expression<String>("breed_d")
    let mColour = Expression<String>("colour_d")
    let mBirth = Expression<Int>("birth_d")
    let mSize = Expression<Int>("size_d")
    let mSex = Expression<Int>("sex_d")
    let mDateCreate = Expression<String>("date_create_u")
    let mDateUpdate = Expression<String>("date_update_u")
    let mDelete = Expression<Int>("delete_u")
    
    
    // Creation of db connection
    init(){
        do {
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("users").appendingPathExtension("sqlite3")
            mDatabase = try Connection(fileUrl.path)
            //1print("Connected to " + tableName + " db, path: " + fileUrl.path)
        } catch {
            print(error)
        }
        self.createTable()
    }
    
    // Creation of the user database
    func createTable() {
        let createTable = mTable.create(ifNotExists: true) { (table) in
            table.column(mId, primaryKey: true)
            table.column(mIdNfc)
            table.column(mIdUser)
            table.column(mName)
            table.column(mBreed)
            table.column(mColour)
            table.column(mBirth)
            table.column(mSize)
            table.column(mSex)
            table.column(mDateCreate)
            table.column(mDateUpdate)
            table.column(mDelete)
        }
        do {
            try mDatabase.run(createTable)
        } catch {
            print(error)
        }
    }
    
    func getById(id: Int) -> User?{
        /*
        var userFound: User?
        
        do {
            let query = mTable.filter(mId == id)
            let users = try mDatabase.prepare(query)
            for user in users {
                // Get only if he's not deleted
                if user[mDelete] == UserDbManager.STATUS.ACTIVE {
                    userFound = User(id: user[mId], name: user[mName], surname: user[mSurname], phone: user[mPhone],
                                     birth: user[mBirth], email: user[mEmail], password: user[mPassword],
                                     dateCreate: user[mDateCreate], dateUpdate: user[mDateUpdate], delete: user[mDelete])
                }
                break
            }
        } catch {
            print(error)
        }
        return userFound
 */
        return nil
    }
    
    
    
    func insert(dog: Dog) -> Bool {
        /*
        let query = mTable.insert(mId <- user.mId, mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                  mPhone <- user.mPhone, mBirth <- user.mBirth, mEmail <- user.mEmail,
                                  mPassword <- user.mPassword, mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                  mCurrent <- current, mDelete <- user.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred user: \(user.mEmail!)")
            return true
        } catch {
            print(error)
            return false
        }
 */
        return false
    }
    
    // Update a specific user
    func update(dog: Dog) -> Bool {
        /*
        let dbUser = mTable.filter(mId == user.mId)
        let updateUser = dbUser.update(mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                       mPhone <- user.mPhone, mBirth <- user.mBirth, mPassword <- user.mPassword,
                                       mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                       mCurrent <- current, mDelete <- user.mDelete)
        do {
            try mDatabase.run(updateUser)
            print("User \(user.mEmail) updated")
            return true
        } catch {
            print(error)
            return false
        }
 */
        return false
    }
    
    // Delete the current user
    func delete(dog: Dog) -> Bool {
        /*
        let notCurrent = UserDbManager.STATUS.NOT_CURRENT
        let deleted = UserDbManager.STATUS.DELETED
        let dbUser = mTable.filter(mId == user.mId)
        let updateUser = dbUser.update(mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                       mPhone <- user.mPhone, mBirth <- user.mBirth, mPassword <- user.mPassword,
                                       mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                       mCurrent <- notCurrent, mDelete <- deleted)
        do {
            try mDatabase.run(updateUser)
            print("User \(user.mEmail!) deleted")
            return true
        } catch {
            print(error)
            return false
        }
 */
        return false
    }
}
