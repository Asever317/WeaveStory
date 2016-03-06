package com.asever.weavestory.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.GalleryData;
import com.asever.weavestory.datamodel.GalleryDataPath;
import com.asever.weavestory.util.OnItemClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Asever on 2015-12-26.
 */
public class GalleryListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<GalleryData> _listData;
    private ArrayList<String> _listDate;

    private int chk_count;
    private OnItemClickListener onItemClickListener;

    public GalleryListAdapter(Context context, ArrayList<GalleryData> listData) {
        this.mContext = context;
        this._listData = listData;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public GalleryListAdapter(Context context) {
        this.mContext = context;
    }

    public ArrayList<String> getAllpath() {
        ArrayList<String> allPath = new ArrayList<>();
        _listDate = new ArrayList<>();
        int loopCount = _listData.size();
        for (int i = 0; i < loopCount; i++) {
            for (int k = 0; k < _listData.get(i).dataPath.size(); k++) {
                if (_listData.get(i).dataPath.get(k).isSeleted) {
//                    allPath[z++] = _listData.get(i).dataPath.get(k).getDataPath1();
                    allPath.add(_listData.get(i).dataPath.get(k).getDataPath1());
                    _listDate.add(_listData.get(i).dataPath.get(k).getImgDate1());
//                    albumData.getImgPaths().add(_listData.get(i).dataPath.get(k).getDataPath1());
//                    albumData.getContentData()
                }
                if (_listData.get(i).dataPath.get(k).isSeleted2) {
//                    allPath[z++] = _listData.get(i).dataPath.get(k).getDataPath2();
                    allPath.add(_listData.get(i).dataPath.get(k).getDataPath2());
                    _listDate.add(_listData.get(i).dataPath.get(k).getImgDate2());
//                    albumData.getContentData().add(_listData.get(i).dataPath.get(k).getDataPath2());
                }
                if (_listData.get(i).dataPath.get(k).isSeleted3) {
//                    allPath[z++] = _listData.get(i).dataPath.get(k).getDataPath3();
                    allPath.add(_listData.get(i).dataPath.get(k).getDataPath3());
                    _listDate.add(_listData.get(i).dataPath.get(k).getImgDate3());
//                    albumData.getImgPaths().add(_listData.get(i).dataPath.get(k).getDataPath3());
                }
            }
        }
        return allPath;
    }

    public ArrayList<String> getPhotoDates() {
        return _listDate;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listData.get(groupPosition).dataPath.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public int getChildTypeCount() {
        return 4;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return this._listData.get(groupPosition).dataPath.get(childPosition).getVIEW_TYPE();
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        int view_type = getChildType(groupPosition, childPosition);
        String path = ((GalleryDataPath) getChild(groupPosition, childPosition)).getDataPath1();
        String path2 = ((GalleryDataPath) getChild(groupPosition, childPosition)).getDataPath2();
        String path3 = ((GalleryDataPath) getChild(groupPosition, childPosition)).getDataPath3();

        if (view_type == 1) {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();

                convertView = infalInflater.inflate(R.layout.gallery_pick_item, null);
                holder.imgQueue = (ImageView) convertView
                        .findViewById(R.id.imgQueue);
                holder.imgQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);
                holder.imgQueueMultiSelected2 = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected2);
                holder.imgQueueMultiSelected3 = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected3);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            Picasso.with(mContext).load(path).fit().into(holder.imgQueue);
            Glide.with(mContext).load(path).fitCenter().into(holder.imgQueue);
//            ImageLoader.getInstance().displayImage(path, holder.imgQueue, options, animateFirstListener);
            holder.imgQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = true;
                        chk_count++;
                    }
                    holder.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
                    onItemClickListener.onClick(chk_count);

                }
            });
            holder.imgQueueMultiSelected2.setVisibility(View.INVISIBLE);
            holder.imgQueueMultiSelected3.setVisibility(View.INVISIBLE);
            holder.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
        } else if (view_type == 2) {
            final ViewHolder2 holder2;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder2 = new ViewHolder2();

                convertView = infalInflater.inflate(R.layout.gallery_pick_item, null);
                holder2.imgQueue = (ImageView) convertView
                        .findViewById(R.id.imgQueue);
                holder2.imgQueue2 = (ImageView) convertView
                        .findViewById(R.id.imgQueue2);
                holder2.imgQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);
                holder2.imgQueueMultiSelected2 = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected2);
                holder2.imgQueueMultiSelected3 = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected3);
                convertView.setTag(holder2);

            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
//            Picasso.with(mContext).load(path).fit().into(holder2.imgQueue);
//            Picasso.with(mContext).load(path2).fit().into(holder2.imgQueue2);
            Glide.with(mContext).load(path).fitCenter().into(holder2.imgQueue);
            Glide.with(mContext).load(path2).fitCenter().into(holder2.imgQueue2);
            holder2.imgQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = true;
                        chk_count++;
                    }
                    holder2.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
                    onItemClickListener.onClick(chk_count);
                }
            });
            holder2.imgQueue2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2 = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2 = true;
                        chk_count++;
                    }
                    holder2.imgQueueMultiSelected2.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2);
                    onItemClickListener.onClick(chk_count);
                }
            });
            holder2.imgQueueMultiSelected3.setVisibility(View.INVISIBLE);
            holder2.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
            holder2.imgQueueMultiSelected2.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2);
        } else if (view_type == 3) {
            final ViewHolder3 holder3;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder3 = new ViewHolder3();

                convertView = infalInflater.inflate(R.layout.gallery_pick_item, null);
                holder3.imgQueue = (ImageView) convertView
                        .findViewById(R.id.imgQueue);
                holder3.imgQueue2 = (ImageView) convertView
                        .findViewById(R.id.imgQueue2);
                holder3.imgQueue3 = (ImageView) convertView
                        .findViewById(R.id.imgQueue3);

                holder3.imgQueueMultiSelected = (ImageView) convertView
                        .findViewById(R.id.imgQueueMultiSelected);
                holder3.imgQueueMultiSelected2 = (ImageView) convertView
                        .findViewById(R.id.imgQueueMultiSelected2);
                holder3.imgQueueMultiSelected3 = (ImageView) convertView
                        .findViewById(R.id.imgQueueMultiSelected3);
                convertView.setTag(holder3);

            } else {
                holder3 = (ViewHolder3) convertView.getTag();
            }

//            Picasso.with(mContext).load(path).fit().into(holder3.imgQueue);
//            Picasso.with(mContext).load(path2).fit().into(holder3.imgQueue2);
//            Picasso.with(mContext).load(path3).fit().into(holder3.imgQueue3);

            Glide.with(mContext).load(path).fitCenter().into(holder3.imgQueue);
            Glide.with(mContext).load(path2).fitCenter().into(holder3.imgQueue2);
            Glide.with(mContext).load(path3).fitCenter().into(holder3.imgQueue3);
            holder3.imgQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted = true;
                        chk_count++;
                    }
                    holder3.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
                    onItemClickListener.onClick(chk_count);
                }
            });
            holder3.imgQueue2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2 = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2 = true;
                        chk_count++;
                    }
                    holder3.imgQueueMultiSelected2.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2);
                    onItemClickListener.onClick(chk_count);
                }
            });
            holder3.imgQueue3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted3) {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted3 = false;
                        chk_count--;
                    } else {
                        ((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted3 = true;
                        chk_count++;
                    }
                    holder3.imgQueueMultiSelected3.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted3);
                    onItemClickListener.onClick(chk_count);
                }
            });

            holder3.imgQueueMultiSelected.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted);
            holder3.imgQueueMultiSelected2.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted2);
            holder3.imgQueueMultiSelected3.setSelected(((GalleryDataPath) getChild(groupPosition, childPosition)).isSeleted3);
        }
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this._listData.get(groupPosition).dataPath.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listData.get(groupPosition).photoDate;
    }

    @Override
    public int getGroupCount() {
        return this._listData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.gallery_pick_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        TextView lblImgCount = (TextView) convertView.findViewById(R.id.lblImgcount);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        lblImgCount.setText("  (" + getPhotoCount(groupPosition) + "장)");

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        return convertView;
    }

    private int getPhotoCount(int groupPosition) {
        int childrenCount = 0;
        for (int i = 0; i < _listData.get(groupPosition).dataPath.size(); i++) {
            if (_listData.get(groupPosition).dataPath.get(i).getDataPath1() != null)
                childrenCount++;
            if (_listData.get(groupPosition).dataPath.get(i).getDataPath2() != null)
                childrenCount++;
            if (_listData.get(groupPosition).dataPath.get(i).getDataPath3() != null)
                childrenCount++;

        }
        return childrenCount;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
        ImageView imgQueueMultiSelected2;
        ImageView imgQueueMultiSelected3;
    }

    public class ViewHolder2 {
        ImageView imgQueue;
        ImageView imgQueue2;

        ImageView imgQueueMultiSelected;
        ImageView imgQueueMultiSelected2;
        ImageView imgQueueMultiSelected3;
    }

    public class ViewHolder3 {
        ImageView imgQueue;
        ImageView imgQueue2;
        ImageView imgQueue3;

        ImageView imgQueueMultiSelected;
        ImageView imgQueueMultiSelected2;
        ImageView imgQueueMultiSelected3;
    }

}