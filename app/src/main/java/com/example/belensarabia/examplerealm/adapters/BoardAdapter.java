package com.example.belensarabia.examplerealm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.belensarabia.examplerealm.R;
import com.example.belensarabia.examplerealm.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by belensarabia on 6/5/18.
 */

public class BoardAdapter extends BaseAdapter {
    private Context context;
    private List<Board> list;
    private int layout;

    public BoardAdapter(Context context, List<Board> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView.findViewById(R.id.textViewBoardTitle);
            vh.notes = (TextView) convertView.findViewById(R.id.textViewBoardNotes);
            vh.createAt = (TextView) convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Board  board = list.get(position);
        vh.title.setText(board.getTitle());

        int numberOfNotes = board.getNotes().size();
        String textForNotes = (numberOfNotes == 1) ? numberOfNotes + " Note" : numberOfNotes + "Notes";
        vh.notes.setText(textForNotes);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(board.getCreatedAt());
        vh.createAt.setText(createdAt);

        return convertView;
    }

    public class ViewHolder{
        TextView title;
        TextView notes;
        TextView createAt;
    }
}
