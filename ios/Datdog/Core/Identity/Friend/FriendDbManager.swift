//
//  FriendDbManager.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import SQLite
import UIKit

class FriendDbManager {
    
    
    var mDatabase: Connection!
    let tableName = "friend"
    let mTable = Table("friend")
    
    let mId = Expression<String>("id")
    let mIdUser = Expression<Int>("id_user_f")
    let mIdFriend = Expression<Int>("id_friend_f")
    let mDateCreate = Expression<String>("date_create_u")
    let mDateUpdate = Expression<String>("date_update_u")
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
        // CREATE TABLE
        let createTable = mTable.create(ifNotExists: true) { (table) in
            table.column(mId, primaryKey: true)
            table.column(mIdUser)
            table.column(mIdFriend)
            table.column(mDateCreate)
            table.column(mDateUpdate)
            table.column(mDelete)
            table.foreignKey(mIdUser, references: user, idUser)
            table.foreignKey(mIdFriend, references: user, idUser)
            // FOREIGN KEY("user_id") REFERENCES "users"("id") ON DELETE SET NULL
        }
        do {
            try mDatabase.run(createTable)
        } catch {
            print(error)
        }
    }
    
    func getAll(user: User) -> Array<Friend>? {
        var allFriends = Array<Friend>()
        
        do {
            let query = mTable.filter(mIdUser == user.mId)
            let friends = try mDatabase.prepare(query)
            for friend in friends {
                let curFriend = Friend(id: friend[mId], idUser: friend[mIdUser], idFriend: friend[mIdFriend],
                                    dateCreate: friend[mDateCreate], dateUpdate: friend[mDateUpdate], delete: friend[mDelete])
                allFriends.append(curFriend)
            }
        } catch {
            print(error)
        }
        return allFriends
    }
    
    func getById(id: String) -> Friend?{
        var friendFound: Friend?
        
        do {
            let query = mTable.filter(mId == id)
            let friend = try mDatabase.prepare(query)
            for friend in friend {
                // Get only if he's not deleted
                if friend[mDelete] == UtilProj.DBSTATUS.AVAILABLE {
                    friendFound = Friend(id: friend[mId], idUser: friend[mIdUser], idFriend: friend[mIdFriend],
                                           dateCreate: friend[mDateCreate], dateUpdate: friend[mDateUpdate], delete: friend[mDelete])
                }
                break
            }
        } catch {
            print(error)
        }
        return friendFound
    }
    
    // Inser a friend
    func insert(friend: Friend) -> Bool {
        let query = mTable.insert(mId <- friend.mId, mIdUser <- friend.mIdUser, mIdFriend <- friend.mIdFriend,
                                  mDateCreate <- friend.mDateCreate, mDateUpdate <- friend.mDateUpdate, mDelete <- friend.mDelete)
        do {
            try mDatabase.run(query)
            print("Registred dog: \(friend.mId!)")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Update a specific dog
    func update(friend: Friend) -> Bool {
        // VERY IMPORTANT -> UPDATE DATE
        friend.mDateUpdate = UtilProj.getDateNow()
        
        let dbFriend = mTable.filter(mId == friend.mId)
        let query = dbFriend.update( mIdUser <- friend.mIdUser, mIdFriend <- friend.mIdFriend,
                                  mDateCreate <- friend.mDateCreate, mDateUpdate <- friend.mDateUpdate, mDelete <- friend.mDelete)
        do {
            try mDatabase.run(query)
            print("Dog \(friend.mId) updated")
            return true
        } catch {
            print(error)
            return false
        }
    }
    
    // Delete the current user
    func delete(friend: Friend) -> Bool {
        friend.mDelete = UtilProj.DBSTATUS.DELETE
        return self.update(friend:friend)
    }
}
