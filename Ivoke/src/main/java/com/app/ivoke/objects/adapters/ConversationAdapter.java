package com.app.ivoke.objects.adapters;

import java.util.List;
import com.app.ivoke.R;
import com.app.ivoke.helpers.DateTimeHelper;
import com.app.ivoke.objects.Conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter {

    LayoutInflater lInflater;
    List<Conversation> conversations;
    int itemAtual;
    Context context;

    public ConversationAdapter(Context pContext, List<Conversation> pConversations) {
        lInflater = LayoutInflater.from(pContext);
        conversations     = pConversations;
        context = pContext;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    @Override
    public int getCount() {
        return conversations!=null? conversations.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return conversations!=null? conversations.get(position) : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listTemplateView;

        if((listTemplateView = convertView) == null)
            listTemplateView = lInflater.inflate(R.layout.main_conversation_list_template
                                                , null);

        TextView txtNome     =
                (TextView) listTemplateView.findViewById(R.id.main_conversation_user_name);
        TextView txtMensagem =
                (TextView) listTemplateView.findViewById(R.id.main_conversation_last_message);
        TextView txtQuando =
                (TextView) listTemplateView.findViewById(R.id.main_conversation_when);

        txtNome.setText(conversations.get(position).getUserOneNome());
        txtMensagem.setText(conversations.get(position).getLastMessage());

        int minutes = (conversations.get(position).getTime()/1000)/60;
        String text = context.getString(R.string.def_description_now);
        if(DateTimeHelper.getHoursFromMinutes(minutes)>0)
           text = String.format(context.getString(R.string.def_description_hours), DateTimeHelper.getHoursFromMinutes(minutes));
        else if(DateTimeHelper.getDaysFromMinutes(minutes)>0)
           text = String.format(context.getString(R.string.def_description_days), DateTimeHelper.getDaysFromMinutes(minutes));
        txtQuando.setText(text);

        return listTemplateView;

    }

    class auxItemList{
        String nomeUsuario;
        String texto;
    }

    public void setData(List<Conversation> pConversations) {
        conversations = pConversations;
    }
}
