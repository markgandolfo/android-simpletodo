package com.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	EditText etNewItem;
	Button btnAddItem;
	
	// Dialog
	Dialog editDialog;
	Button savebtn;
	Button canbtn;
	EditText etEditItem;
	String item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		etNewItem = (EditText)
				findViewById(R.id.etNewItem);
		minimizeKeyboard();
		setupListViewListener();
	}

	public void addTodoItem(View v) {
		itemsAdapter.add(etNewItem.getText().toString());
		etNewItem.setText("");
		saveItems();
	}
	
	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, 
					View view, int position, long rowId) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, final int position, long rowId) {
				editDialog = new Dialog(TodoActivity.this);
                editDialog.setContentView(R.layout.dialog);
                editDialog.setTitle("Edit Item");

                savebtn = (Button)editDialog.findViewById(R.id.savebtn);
				canbtn = (Button)editDialog.findViewById(R.id.canbtn);

                etEditItem = (EditText)editDialog.findViewById(R.id.etEditItem);
				etEditItem.setText(items.get(position));

				savebtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						item = etEditItem.getText().toString();
						items.set(position, item);
						itemsAdapter.notifyDataSetChanged();
						saveItems();
						minimizeKeyboard();
						editDialog.dismiss();
					}
				});

				canbtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						editDialog.dismiss();
					}
				});
                
				editDialog.show();
			}
		});
	}
	
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}

	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void minimizeKeyboard() {
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}
}
