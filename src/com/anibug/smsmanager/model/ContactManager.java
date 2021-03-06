package com.anibug.smsmanager.model;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.anibug.smsmanager.model.Contact.DataBase;
import com.anibug.smsmanager.model.filter.Filter;


public class ContactManager extends Manager<Contact> implements Filter {
	public ContactManager(Context context) {
		super(context);
		
		this.context = context;
	}
	
	Context context;
	public static final int MIN_PHONENUMBER_LENGTH_IN_CHINA = 11;
	
	private Set<String> phoneNumbers = null;

	public boolean hasPhoneNumber(String number) {
		if (phoneNumbers == null)
			phoneNumbers = getAllPhoneNumbers();

		return phoneNumbers.contains(normalizePhoneNumber(number));
	}

	// Whether it is good to truncate phone numbers?
	public String normalizePhoneNumber(String number) {
		if (number.length() <= MIN_PHONENUMBER_LENGTH_IN_CHINA)
			return number;
		return number.substring(number.length() - MIN_PHONENUMBER_LENGTH_IN_CHINA);
	}

	public Set<String> getAllPhoneNumbers() {
		String[] columns = new String[] { DataBase.PHONENUMBER };
		Cursor cursor = getSqliteDatabase().query(getTableName(), columns, null, null, null, null, null);
		
		HashSet<String> numbers = new HashSet<String>();
		if (cursor.moveToFirst()) {
			do {
				// We didn't normalize/truncate phone numbers before saving to database. 
				numbers.add(normalizePhoneNumber(cursor.getString(0)));
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		
		return numbers;
	}
	
	@Override
	public String getTableName() {
		return DataBase.TABLE_NAME;
	}

	@Override
	public String[] getTableDefinitionSQLs() {
		String[] result = new String[1];
		String tableFormat = "Create Table %s (" +
				"id INTEGER Primary Key," +
				"%s VARCHAR[50] Unique," +
				"%s VARCHAR[50] Unique," +
				"%s INTEGER" +
				")";
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(), DataBase.NAME,
				DataBase.PHONENUMBER, DataBase.STATUS);
		result[0] = formatter.toString();
		return result;
	}

	@Override
	public ContentValues createRecord(Contact message) {
		ContentValues values = new ContentValues();
		values.put(DataBase.NAME, message.getName());
		values.put(DataBase.PHONENUMBER, message.getPhoneNumber());
		values.put(DataBase.STATUS, message.getStatus());
		
		return values;	
	}

	@Override
	public Contact createObject(Cursor cursor) {
		final int indexName = cursor.getColumnIndexOrThrow(DataBase.NAME);
		final int indexPhoneNumber = cursor.getColumnIndexOrThrow(DataBase.PHONENUMBER);
		final int indexStatus = cursor.getColumnIndexOrThrow(DataBase.STATUS);
		Contact contact = new Contact(
				cursor.getString(indexName),
				cursor.getString(indexPhoneNumber),
				cursor.getInt(indexStatus));
		return contact;
	}

	public boolean match(Message message) {
		return hasPhoneNumber(message.getPhoneNumber());
	}
	
	public Contact getContactFromPickResult(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        String[] cols = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};
		Cursor contactCur = contentResolver.query(uri, cols, null, null, null);

		if (!contactCur.moveToFirst())
			return null;

        String name = contactCur.getString(contactCur.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
		String number = contactCur.getString(contactCur.getColumnIndexOrThrow(Phone.NUMBER));

		if(!contactCur.isClosed())
			contactCur.close();

		return new Contact(name, number.replace("-", ""));
	}
}
