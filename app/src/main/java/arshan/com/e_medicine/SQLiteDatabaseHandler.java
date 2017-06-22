package arshan.com.e_medicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import arshan.com.e_medicine.Models.CategoriesSQLite;
import arshan.com.e_medicine.Models.DistributorsSQLite;
import arshan.com.e_medicine.Models.ProductsSQLite;
import arshan.com.e_medicine.Models.PurchasesPojo;
import arshan.com.e_medicine.Models.UsersPojo;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "MyDB";

	private static final String TABLE_PRODUCTS = "products";

	private static final String TABLE_DISTRIBUTORS = "distributors";

	private static final String TABLE_CATEGORIES = "categories";

	private static final String TABLE_PURCHASES = "purchases";

	private static final String TABLE_USERS = "users";

	public SQLiteDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE "+ TABLE_PRODUCTS +"(id TEXT PRIMARY KEY, companyid TEXT, barcode TEXT, itemname TEXT, mfgdate TEXT, expdate TEXT, maxdiscount TEXT, qty TEXT, mrp TEXT, batch TEXT, bmp BLOB)" ;
		String CREATE_DISTRIBUTORS_TABLE = "CREATE TABLE "+ TABLE_DISTRIBUTORS +"(id TEXT PRIMARY KEY, companyid TEXT, name TEXT, email TEXT, uname TEXT, password TEXT, mobile TEXT, phone TEXT, isActive TEXT, picURL TEXT, createdBy TEXT, modifiedBy TEXT, createdOn TEXT, modifiedOn TEXT, bmp BLOB)" ;
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE "+ TABLE_CATEGORIES +"(id TEXT PRIMARY KEY, companyid TEXT, name TEXT, createdBy TEXT, createdOn TEXT, modifiedBy TEXT, modifiedOn TEXT)" ;
		String CREATE_PURCHASES_TABLE = "CREATE TABLE "+ TABLE_PURCHASES +"(id TEXT PRIMARY KEY, companyid TEXT, BillDate TEXT, InvoiceNumber TEXT, DistributorId TEXT, Amount TEXT, PaymentDate TEXT, PaymentMode TEXT, ChequeNumber TEXT, BankName TEXT, createdBy TEXT, createdOn TEXT, modifiedBy TEXT, modifiedOn TEXT, isSettled TEXT)" ;
		String CREATE_USERS_TABLE = "CREATE TABLE "+ TABLE_USERS +"(id TEXT PRIMARY KEY, fname TEXT, lname TEXT, uname TEXT, password TEXT, gender TEXT, email TEXT, mobile TEXT, phone TEXT, usertype TEXT, apikey TEXT, addressId TEXT, profilePic TEXT, companyid TEXT, createdBy TEXT, createdOn TEXT, modifiedBy TEXT, modifiedOn TEXT, isActive TEXT, bmp BLOB)" ;

		db.execSQL(CREATE_PRODUCTS_TABLE);
		db.execSQL(CREATE_DISTRIBUTORS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_PURCHASES_TABLE);
		db.execSQL(CREATE_USERS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTORS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	void deleteAllTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTORS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	void addProduct(ProductsSQLite productsSQLite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", productsSQLite.getId());
		values.put("companyid", productsSQLite.getCompanyid());
		values.put("barcode", productsSQLite.getBarcode());
		values.put("itemname", productsSQLite.getItemname());
		values.put("mfgdate", productsSQLite.getMfgdate());
		values.put("expdate", productsSQLite.getExpdate());
		values.put("maxdiscount", productsSQLite.getMaxdiscount());
		values.put("qty", productsSQLite.getQty());
		values.put("mrp", productsSQLite.getMrp());
		values.put("batch", productsSQLite.getBatch());
		values.put("bmp", productsSQLite.getImageByteArray());
		try {
			// Inserting Row
			db.insert(TABLE_PRODUCTS, null, values);
			db.close(); // Closing database connection
		} catch (SQLiteConstraintException e) {
			Log.d("SQConstraintException", "" + e.getLocalizedMessage());
		} catch (SQLiteException e) {
			Log.d("SQLiteException", "" + e.getLocalizedMessage());
		} catch (Exception e) {
			Log.d("Exception", "" + e.getLocalizedMessage());
		}
	}

	void addDistributor(DistributorsSQLite distributorsSQLite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", distributorsSQLite.getId());
		values.put("companyid", distributorsSQLite.getCompanyid());
		values.put("name", distributorsSQLite.getName());
		values.put("email", distributorsSQLite.getEmail());
		values.put("uname", distributorsSQLite.getUname());
		values.put("password", distributorsSQLite.getPassword());
		values.put("mobile", distributorsSQLite.getMobile());
		values.put("phone", distributorsSQLite.getPhone());
		values.put("isActive", distributorsSQLite.getIsActive());
		values.put("picURL", distributorsSQLite.getPicURL());
		values.put("createdBy", distributorsSQLite.getCreatedBy());
		values.put("modifiedBy", distributorsSQLite.getModifiedBy());
		values.put("createdOn", distributorsSQLite.getCreatedOn());
		values.put("modifiedOn", distributorsSQLite.getModifiedOn());
		values.put("bmp", distributorsSQLite.getImageByteArray());

		try {
			// Inserting Row
			db.insert(TABLE_DISTRIBUTORS, null, values);
			db.close(); // Closing database connection
		} catch (SQLiteConstraintException e) {
			Log.d("SQConstraintException", ""+e.getLocalizedMessage());
		} catch (SQLiteException e) {
			Log.d("SQLiteException", ""+e.getLocalizedMessage());
		} catch (Exception e) {
			Log.d("Exception", ""+e.getLocalizedMessage());
		}
	}

	void addCategory(CategoriesSQLite categoriesSQLite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", categoriesSQLite.getId());
		values.put("companyid", categoriesSQLite.getCompanyid());
		values.put("name", categoriesSQLite.getName());
		values.put("createdBy", categoriesSQLite.getCreatedBy());
		values.put("createdOn", categoriesSQLite.getCreatedOn());
		values.put("modifiedBy", categoriesSQLite.getModifiedBy());
		values.put("modifiedOn", categoriesSQLite.getModifiedOn());
		try {
			// Inserting Row
			db.insert(TABLE_CATEGORIES, null, values);
			db.close(); // Closing database connection
		} catch (SQLiteConstraintException e) {
			Log.d("SQConstraintException", "" + e.getLocalizedMessage());
		} catch (SQLiteException e) {
			Log.d("SQLiteException", "" + e.getLocalizedMessage());
		} catch (Exception e) {
			Log.d("Exception", "" + e.getLocalizedMessage());
		}
	}

	void addPurchase(PurchasesPojo purchasesPojo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", purchasesPojo.getId());
		values.put("companyid", purchasesPojo.getCompanyid());
		values.put("BillDate", purchasesPojo.getBillDate());
		values.put("InvoiceNumber", purchasesPojo.getInvoiceNumber());
		values.put("DistributorId", purchasesPojo.getDistributorId());
		values.put("Amount", purchasesPojo.getAmount());
		values.put("PaymentDate", purchasesPojo.getPaymentDate());
		values.put("PaymentMode", purchasesPojo.getPaymentMode());
		values.put("ChequeNumber", purchasesPojo.getChequeNumber());
		values.put("BankName", purchasesPojo.getBankName());
		values.put("createdBy", purchasesPojo.getCreatedBy());
		values.put("createdOn", purchasesPojo.getCreatedOn());
		values.put("modifiedBy", purchasesPojo.getModifiedBy());
		values.put("modifiedOn", purchasesPojo.getModifiedOn());
		values.put("isSettled", purchasesPojo.getIsSettled());
		try {
			// Inserting Row
			db.insert(TABLE_PURCHASES, null, values);
			db.close(); // Closing database connection
		} catch (SQLiteConstraintException e) {
			Log.d("SQConstraintException", "" + e.getLocalizedMessage());
		} catch (SQLiteException e) {
			Log.d("SQLiteException", "" + e.getLocalizedMessage());
		} catch (Exception e) {
			Log.d("Exception", "" + e.getLocalizedMessage());
		}
	}

	void addUser(UsersPojo usersPojo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", usersPojo.getId());
		values.put("fname", usersPojo.getFname());
		values.put("lname", usersPojo.getLname());
		values.put("uname", usersPojo.getUname());
		values.put("password", usersPojo.getPassword());
		values.put("gender", usersPojo.getGender());
		values.put("email", usersPojo.getEmail());
		values.put("mobile", usersPojo.getMobile());
		values.put("phone", usersPojo.getPhone());
		values.put("usertype", usersPojo.getUsertype());
		values.put("apikey", usersPojo.getApikey());
		values.put("addressId", usersPojo.getAddressId());
		values.put("profilePic", usersPojo.getProfilePic());
		values.put("companyid", usersPojo.getCompanyid());
		values.put("createdBy", usersPojo.getCreatedBy());
		values.put("modifiedBy", usersPojo.getModifiedBy());
		values.put("createdOn", usersPojo.getCreatedOn());
		values.put("modifiedOn", usersPojo.getModifiedOn());
		values.put("isActive", usersPojo.getIsActive());
		values.put("bmp", usersPojo.getImageByteArray());

		try {
			// Inserting Row
			db.insert(TABLE_USERS, null, values);
			db.close(); // Closing database connection
		} catch (SQLiteConstraintException e) {
			Log.d("SQConstraintException", ""+e.getLocalizedMessage());
		} catch (SQLiteException e) {
			Log.d("SQLiteException", ""+e.getLocalizedMessage());
		} catch (Exception e) {
			Log.d("Exception", ""+e.getLocalizedMessage());
		}
	}

	// Getting single product
	/*ProductsSQLite getProduct(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ProductsSQLite product = new ProductsSQLite(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return product;
	}*/
	
	// Getting All products
	public List<ProductsSQLite> getAllProducts() {
		List<ProductsSQLite> productList = new ArrayList<ProductsSQLite>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ProductsSQLite product = new ProductsSQLite();
				product.setId(cursor.getString(0));
				product.setCompanyid(cursor.getString(1));
				product.setBarcode(cursor.getString(2));
				product.setItemname(cursor.getString(3));
				product.setMfgdate(cursor.getString(4));
				product.setExpdate(cursor.getString(5));
				product.setMaxdiscount(cursor.getString(6));
				product.setQty(cursor.getString(7));
				product.setMrp(cursor.getString(8));
				product.setBatch(cursor.getString(9));
				product.setImageByteArray(cursor.getBlob(10));
				// Adding product to list
				productList.add(product);
			} while (cursor.moveToNext());
		}

		// return product list
		return productList;
	}

	// Getting All distrbutors
	public List<DistributorsSQLite> getAllDistributors() {
		List<DistributorsSQLite> distributorList = new ArrayList<DistributorsSQLite>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DISTRIBUTORS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DistributorsSQLite distributor = new DistributorsSQLite();
				distributor.setId(cursor.getString(0));
				distributor.setCompanyid(cursor.getString(1));
				distributor.setName(cursor.getString(2));
				distributor.setEmail(cursor.getString(3));
				distributor.setUname(cursor.getString(4));
				distributor.setPassword(cursor.getString(5));
				distributor.setMobile(cursor.getString(6));
				distributor.setPhone(cursor.getString(7));
				distributor.setIsActive(cursor.getString(8));
				distributor.setPicURL(cursor.getString(9));
				distributor.setCreatedBy(cursor.getString(10));
				distributor.setModifiedBy(cursor.getString(11));
				distributor.setCreatedOn(cursor.getString(12));
				distributor.setModifiedOn(cursor.getString(13));
				distributor.setImageByteArray(cursor.getBlob(14));

				// Adding distributor to list
				distributorList.add(distributor);
			} while (cursor.moveToNext());
		}
		// return distributors list
		return distributorList;
	}

	// Getting All Categories
	public List<CategoriesSQLite> getAllCategories() {
		List<CategoriesSQLite> categoryList = new ArrayList<CategoriesSQLite>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CategoriesSQLite category = new CategoriesSQLite();
				category.setId(cursor.getString(0));
				category.setCompanyid(cursor.getString(1));
				category.setName(cursor.getString(2));
				category.setCreatedBy(cursor.getString(3));
				category.setCreatedOn(cursor.getString(4));
				category.setModifiedBy(cursor.getString(5));
				category.setModifiedOn(cursor.getString(6));
				// Adding categories to list
				categoryList.add(category);
			} while (cursor.moveToNext());
		}
		// return categories list
		return categoryList;
	}

	// Getting All purchases
	public List<PurchasesPojo> getAllPurchases() {
		List<PurchasesPojo> purchasesList = new ArrayList<PurchasesPojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PURCHASES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PurchasesPojo purchase = new PurchasesPojo();
				purchase.setId(cursor.getString(0));
				purchase.setCompanyid(cursor.getString(1));
				purchase.setBillDate(cursor.getString(2));
				purchase.setInvoiceNumber(cursor.getString(3));
				purchase.setDistributorId(cursor.getString(4));
				purchase.setAmount(cursor.getString(5));
				purchase.setPaymentDate(cursor.getString(6));
				purchase.setPaymentMode(cursor.getString(7));
				purchase.setChequeNumber(cursor.getString(8));
				purchase.setBankName(cursor.getString(9));
				purchase.setCreatedBy(cursor.getString(10));
				purchase.setCreatedOn(cursor.getString(11));
				purchase.setModifiedBy(cursor.getString(12));
				purchase.setModifiedOn(cursor.getString(13));
				purchase.setIsSettled(cursor.getString(14));
				// Adding purchase to list
				purchasesList.add(purchase);
			} while (cursor.moveToNext());
		}

		// return purchase list
		return purchasesList;
	}

	// Getting All users
	public List<UsersPojo> getAllUsers() {
		List<UsersPojo> usersPojoList = new ArrayList<UsersPojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				UsersPojo user = new UsersPojo();
				user.setId(cursor.getString(0));
				user.setFname(cursor.getString(1));
				user.setLname(cursor.getString(2));
				user.setUname(cursor.getString(3));
				user.setPassword(cursor.getString(4));
				user.setGender(cursor.getString(5));
				user.setEmail(cursor.getString(6));
				user.setMobile(cursor.getString(7));
				user.setPhone(cursor.getString(8));
				user.setApikey(cursor.getString(9));
				user.setAddressId(cursor.getString(10));
				user.setProfilePic(cursor.getString(11));
				user.setCompanyid(cursor.getString(12));
				user.setCreatedBy(cursor.getString(13));
				user.setCreatedOn(cursor.getString(14));
				user.setModifiedBy(cursor.getString(15));
				user.setModifiedOn(cursor.getString(16));
				user.setIsActive(cursor.getString(17));
				user.setImageByteArray(cursor.getBlob(18));

				// Adding distributor to list
				usersPojoList.add(user);
			} while (cursor.moveToNext());
		}
		// return distributors list
		return usersPojoList;
	}

	// Updating single product
	/*public int updateProduct(ProductsSQLite product) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, product.getName());
		values.put(KEY_PH_NO, product.getPhoneNumber());

		// updating row
		return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(product.getID()) });
	}*/

	// Deleting single product
	public void deleteProduct(ProductsSQLite product) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, "id" + " = ?",
				new String[] { String.valueOf(product.getId()) });
		db.close();
	}

	// Deleting single distributor
	public void deleteDistributor(DistributorsSQLite distributor) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DISTRIBUTORS, "id" + " = ?",
				new String[] { String.valueOf(distributor.getId()) });
		db.close();
	}

	// Deleting single category
	public void deleteCategory(CategoriesSQLite category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CATEGORIES, "id" + " = ?",
				new String[] { String.valueOf(category.getId()) });
		db.close();
	}

	// Deleting single purchase
	public void deletePurchase(PurchasesPojo purchase) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PURCHASES, "id" + " = ?",
				new String[] { String.valueOf(purchase.getId()) });
		db.close();
	}

	// Deleting single user
	public void deleteUser(UsersPojo usersPojo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, "id" + " = ?",
				new String[] { String.valueOf(usersPojo.getId()) });
		db.close();
	}

	// Getting products Count
	public int getProductsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Getting distributors Count
	public int getDistributorsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_DISTRIBUTORS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Getting categories Count
	public int getCategoriesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Getting purchases Count
	public int getPurchasesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PURCHASES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Getting distributors Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

}
