package com.example.android.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kezia on 05/07/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter( Context context, List<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {


        View listItemview = convertView;
        if (listItemview == null) {
            listItemview = LayoutInflater.from(getContext()).inflate
                    (R.layout.book_list_item, parent, false);
        }
        Book currentBook  = getItem(position);
        TextView titleView = (TextView) listItemview.findViewById(R.id.text_view_title);
        titleView.setText(currentBook.getTitle());

        TextView authorView = (TextView) listItemview.findViewById(R.id.text_view_author);
        authorView.setText(currentBook.getAuthor());

        return listItemview;
    }
}
