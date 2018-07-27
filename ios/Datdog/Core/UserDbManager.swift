//
//  UserDbManager.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import SQLite
import UIKit

class UserDbManager {
    
    struct STATUS {
        static let CURRENT = 1
        static let NOT_CURRENT = 0
        static let DELETED = 1
        static let ACTIVE = 0
    }
    
    var mDatabase: Connection!
    let tableName = "user"
    let mTable = Table("user")
    let mId = Expression<Int>("id")
    let mName = Expression<String>("name_u")
    let mSurname = Expression<String>("surname_u")
    let mPhone = Expression<String>("phone_u")
    let mBirth = Expression<String>("birth_u")
    let mEmail = Expression<String>("email_u")
    let mPassword = Expression<String>("password_u")
    let mDateCreate = Expression<String>("date_create_u")
    let mDateUpdate = Expression<String>("date_update_u")
    let mCurrent = Expression<Int>("current_u")
    let mDelete = Expression<Int>("delete_u")
    
    
    // Creation of db connection
    init(){
        do {
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("users").appendingPathExtension("sqlite3")
            let database = try Connection(fileUrl.path)
            mDatabase = database
            print("Connected to " + tableName + " db, path: " + fileUrl.path)
        } catch {
            print(error)
        }
        self.createTable()
    }
    
    // Creation of the user database
    func createTable() {
        let createTable = mTable.create(ifNotExists: true) { (table) in
            table.column(mId, primaryKey: true)
            table.column(mName)
            table.column(mSurname)
            table.column(mPhone)
            table.column(mBirth)
            table.column(mEmail, unique: true)
            table.column(mPassword)
            table.column(mDateCreate)
            table.column(mDateUpdate)
            table.column(mCurrent)
            table.column(mDelete)
        }
        do {
            try mDatabase.run(createTable)
        } catch {
            print(error)
        }
    }
    
    func getById(id: Int) -> User?{
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
    }
    
    // Get the user with the active session
    func getCurrent(view: UIViewController!) -> User? {
        var currentUsers = Set<User>()
        do {
            let users = try mDatabase.prepare(mTable)
            for user in users {
                // Get if he's only the current in the session
                if user[mCurrent] == UserDbManager.STATUS.CURRENT {
                    let curUser = User(id: user[mId], name: user[mName], surname: user[mSurname], phone: user[mPhone],
                                   birth: user[mBirth], email: user[mEmail], password: user[mPassword],
                                   dateCreate: user[mDateCreate], dateUpdate: user[mDateUpdate], delete: user[mDelete])
                    currentUsers.insert(curUser)
                }
            }
        } catch {
            print(error)
        }
        
        // Controll if we have only one user logged in
        if currentUsers.count == 1 {
            for user in currentUsers {
                return user
            }
        } else if currentUsers.count > 1 {
            // Logou for all the user and redirect to login page
            for user in currentUsers {
                self.logut(user: user)
            }
            let vc = view.storyboard?.instantiateViewController(withIdentifier: "loginView") as! LoginController
            view.present(vc, animated: true, completion:nil )
        }
        return nil
    }
    
    
    // Login the user
    func login(user: User) -> Bool {
        let current = UserDbManager.STATUS.CURRENT
        // We have to be sure that the user is not deleted
        if user.mDelete == UserDbManager.STATUS.ACTIVE {
            if self.getById(id: user.mId) != nil {
                return self.update(user: user)
            } else {
                return self.insert(user: user,current: current)
            }
        }
        return false
    }
    
    // Logout the user
    func logut(user: User) {
        let notCurrent = UserDbManager.STATUS.NOT_CURRENT
        let deleted = UserDbManager.STATUS.ACTIVE
        let dbUser = mTable.filter(mId == user.mId)
        let updateUser = dbUser.update(mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                       mPhone <- user.mPhone, mBirth <- user.mBirth, mPassword <- user.mPassword,
                                       mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                       mCurrent <- notCurrent, mDelete <- deleted)
        do {
            try mDatabase.run(updateUser)
            print("User \(user.mEmail) logout")
        } catch {
            print(error)
        }
    }
    
    func insert(user: User, current: Int) -> Bool {
        let query = mTable.insert(mId <- user.mId, mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                  mPhone <- user.mPhone, mBirth <- user.mBirth, mEmail <- user.mEmail,
                                  mPassword <- user.mPassword, mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                  mCurrent <- current, mDelete <- user.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred user: \(user.mName)")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Update a specific user
    func update(user: User) -> Bool {
        let dbUser = mTable.filter(mId == user.mId)
        let updateUser = dbUser.update(mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                       mPhone <- user.mPhone, mBirth <- user.mBirth, mPassword <- user.mPassword,
                                       mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate)
        do {
            try mDatabase.run(updateUser)
            print("User \(user.mEmail) updated")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Delete the current user
    func delete(user: User) -> Bool {
        let notCurrent = UserDbManager.STATUS.NOT_CURRENT
        let deleted = UserDbManager.STATUS.DELETED
        let dbUser = mTable.filter(mId == user.mId)
        let updateUser = dbUser.update(mName <- user.mName, mId <- user.mId, mSurname <- user.mSurname,
                                       mPhone <- user.mPhone, mBirth <- user.mBirth, mPassword <- user.mPassword,
                                       mDateCreate <- user.mDateCreate, mDateUpdate <- user.mDateUpdate,
                                       mCurrent <- notCurrent, mDelete <- deleted)
        do {
            try mDatabase.run(updateUser)
            print("User \(user.mEmail) deleted")
            return true
        } catch {
            print(error)
            return false
        }
    }
}
