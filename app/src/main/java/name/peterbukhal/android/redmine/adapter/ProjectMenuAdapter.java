package name.peterbukhal.android.redmine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import name.peterbukhal.android.redmine.R;
import name.peterbukhal.android.redmine.service.model.Project;

import static android.view.LayoutInflater.from;

public class ProjectMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<Project> mProjects = new ArrayList<>();

    public ProjectMenuAdapter(Context context, List<Project> projects) {
        mContext = context;
        mProjects.addAll(projects);
    }

    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public Project getItem(int position) {
        return mProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = from(mContext)
                    .inflate(R.layout.l_project_menu2, parent, false);

            holder = new ViewHolder();
            holder.mTvText = (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvText.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = from(mContext)
                    .inflate(R.layout.l_project_menu, parent, false);

            holder = new ViewHolder();
            holder.mTvText = (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvText.setText(getItem(position).getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView mTvText;
    }

}
