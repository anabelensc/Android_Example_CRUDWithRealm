package com.example.belensarabia.examplerealm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.belensarabia.examplerealm.R;
import com.example.belensarabia.examplerealm.adapters.BoardAdapter;
import com.example.belensarabia.examplerealm.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import io.realm.Realm;

/**
 * Created by belensarabia on 1/5/18.
 */

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener{

    private Realm realm;
    private FloatingActionButton fab;
    private ListView listView;
    private BoardAdapter adapter;

    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_board);

        // Db Realm
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
        listView = (ListView) findViewById(R.id.listViewBoard);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ShowAlertForCreatingBoard("Add new board", "type for a new board");
            }
        });

      registerForContextMenu(listView);
    }

    // CRUD
    private void createNewBoard(final  String boardName){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Board board = new Board(boardName);
                realm.copyToRealm(board);
            }
        });
    }

    private void deleteBoard(final Board board){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                board.deleteFromRealm();
                realm .copyToRealm(board);
            }
        });
    }

    private void editBoard(final String newName, final Board board ){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                board.setTitle(newName);
                realm.copyToRealmOrUpdate(board);
                realm.copyToRealm(board);
            }
        });
    }

    private void deleteAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    // Dialog
    private void ShowAlertForCreatingBoard(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setMessage(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() > 0)
                    createNewBoard(boardName);
                else
                    Toast.makeText(getApplicationContext(), "The name is required to create a new Board", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowAlertForEditingBoard(String title, String message, final Board board){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setMessage(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getTitle());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() == 0){
                    Toast.makeText(getApplicationContext(), "The name is required to edit the current Board", Toast.LENGTH_LONG).show();
                }else if(boardName.equals(board.getTitle())){
                    Toast.makeText(getApplicationContext(), "The name is the same that it was before", Toast.LENGTH_LONG).show();
                }
                else {
                    editBoard(boardName, board);
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_board_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.edit_board:
                ShowAlertForEditingBoard("Edit board", "Change the name of the board", boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onChange(RealmResults<Board> element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }
}
