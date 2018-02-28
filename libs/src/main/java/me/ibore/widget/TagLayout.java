package me.ibore.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.ibore.libs.R;

/**
 * Created by Administrator on 2018/2/28.
 */

public class TagLayout extends FlexboxLayout {

    /*是否展示选中效果*/
    private boolean isShowHighlight = true;
    /*默认和已选的背景*/
    private int itemDefaultDrawable;
    private int itemSelectDrawable;
    /*默认和已选的文字颜色*/
    private int itemDefaultTextColor;
    private int itemSelectTextColor;
    /*操作模式 0 - 多选 | 1 - 单选*/
    private int mode = MODE_MULTI_SELECT;
    /*可选标签的最大数量*/
    private int maxSelection;
    public static final int MODE_MULTI_SELECT = 0;
    public static final int MODE_SINGLE_SELECT = 1;

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        isShowHighlight = ta.getBoolean(R.styleable.TagLayout_showHighlight, true);
        itemDefaultDrawable = ta.getResourceId(R.styleable.TagLayout_defaultDrawable, 0);
        itemSelectDrawable = ta.getResourceId(R.styleable.TagLayout_selectDrawable, 0);
        itemDefaultTextColor = ta.getColor(R.styleable.TagLayout_defaultTextColor, 0);
        itemSelectTextColor = ta.getColor(R.styleable.TagLayout_selectTextColor, 0);
        mode = ta.getInt(R.styleable.TagLayout_mode, MODE_MULTI_SELECT);
        maxSelection = ta.getInt(R.styleable.TagLayout_maxSelectionCount, 0);
        ta.recycle();
    }

    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            removeAllViews();
            return;
        }
        adapter.bindView(this);
        adapter.addTags();
    }

    public boolean isShowHighlight() {
        return isShowHighlight;
    }

    public void setShowHighlight(boolean showHighlight) {
        isShowHighlight = showHighlight;
    }

    public int getItemDefaultDrawable() {
        return itemDefaultDrawable;
    }

    public void setItemDefaultDrawable(int itemDefaultDrawable) {
        this.itemDefaultDrawable = itemDefaultDrawable;
    }

    public int getItemSelectDrawable() {
        return itemSelectDrawable;
    }

    public void setItemSelectDrawable(int itemSelectDrawable) {
        this.itemSelectDrawable = itemSelectDrawable;
    }

    public int getItemDefaultTextColor() {
        return itemDefaultTextColor;
    }

    public void setItemDefaultTextColor(int itemDefaultTextColor) {
        this.itemDefaultTextColor = itemDefaultTextColor;
    }

    public int getItemSelectTextColor() {
        return itemSelectTextColor;
    }

    public void setItemSelectTextColor(int itemSelectTextColor) {
        this.itemSelectTextColor = itemSelectTextColor;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    public static class TagView<T> extends FrameLayout implements View.OnClickListener {
        private int itemDefaultDrawable;
        private int itemSelectDrawable;

        private int itemDefaultTextColor;
        private int itemSelectTextColor;

        private T item;

        public TextView textView;

        private TagWithListener<T> listener;

        private boolean isItemSelected;

        public void setListener(TagWithListener<T> listener) {
            this.listener = listener;
        }

        public TagView(Context context) {
            this(context, null);
        }

        public TagView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            addView(textView);
            setOnClickListener(this);
        }

        /**
         * 设置标签
         *
         * @param item
         */
        public void setItem(T item) {
            this.item = item;
        }

        public T getItem() {
            return item;
        }

        @Override
        public void onClick(View v) {
            if (listener == null) return;
            listener.onItemSelect(item);
        }

        public void selectItemChangeColorState() {
            if (isItemSelected) {
                setBackgroundResource(itemDefaultDrawable);
                textView.setTextColor(itemDefaultTextColor);
                isItemSelected = false;
            } else {
                setBackgroundResource(itemSelectDrawable);
                textView.setTextColor(itemSelectTextColor);
                isItemSelected = true;
            }
        }

        public boolean isItemSelected() {
            return isItemSelected;
        }

        public void setItemSelected(boolean itemSelected) {
            isItemSelected = itemSelected;
            if (itemSelected) {
                setBackgroundResource(itemSelectDrawable);
                textView.setTextColor(itemSelectTextColor);
            } else {
                setBackgroundResource(itemDefaultDrawable);
                textView.setTextColor(itemDefaultTextColor);
            }
        }

        public void setItemDefaultDrawable(int itemDefaultDrawable) {
            this.itemDefaultDrawable = itemDefaultDrawable;
            setBackgroundResource(itemDefaultDrawable);
        }

        public void setItemSelectDrawable(int itemSelectDrawable) {
            this.itemSelectDrawable = itemSelectDrawable;
        }

        public void setItemDefaultTextColor(int itemDefaultTextColor) {
            this.itemDefaultTextColor = itemDefaultTextColor;
            textView.setTextColor(itemDefaultTextColor);
        }

        public void setItemSelectTextColor(int itemSelectTextColor) {
            this.itemSelectTextColor = itemSelectTextColor;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public interface TagWithListener<T> {

        void onItemSelect(T item);

    }

    public interface OnFlexboxSubscribeListener<T> {

        /**
         * @param selectedItem 已选中的标签
         */
        void onSubscribe(List<T> selectedItem);
    }

    public abstract static class Adapter<V extends TagView, T> {
        private Context context;
        /**
         * 根布局
         */
        private TagLayout rootView;

        /**
         * 数据源
         */
        private List<T> source;

        /**
         * 已选项目
         */
        private List<T> selectItems;
        /*view和tag的对应关系*/
        private Map<V, T> viewMap;
        /*标签选择操作的订阅接口*/
        private OnFlexboxSubscribeListener<T> onSubscribeListener;
        /*是否展示选中效果*/
        private boolean isShowHighlight = true;
        /*可选标签的最大数量*/
        private int maxSelection;
        /*默认和已选的背景*/
        protected int itemDefaultDrawable;
        protected int itemSelectDrawable;
        /*默认和已选的文字颜色*/
        protected int itemDefaultTextColor;
        protected int itemSelectTextColor;
        /*操作模式 0 - 多选 | 1 - 单选*/
        private int mode;

        public void setOnSubscribeListener(OnFlexboxSubscribeListener<T> onSubscribeListener) {
            this.onSubscribeListener = onSubscribeListener;
        }

        public void setSource(List<T> source) {
            this.source = source;
        }

        public void setSelectItems(List<T> selectItems) {
            this.selectItems = selectItems;
        }

        public List<T> getSelectItems() {
            return selectItems;
        }

        public Adapter(Context context, List<T> source) {
            this.context = context;
            this.source = source;
            viewMap = new ArrayMap<>();
        }

        public Adapter(Context context, List<T> source, List<T> selectItems) {
            this.context = context;
            this.source = source;
            this.selectItems = selectItems;
            viewMap = new ArrayMap<>();
        }

        public Context getContext() {
            return context;
        }

        public List<T> getData() {
            return source;
        }

        /**
         * 绑定控件
         *
         * @param rootView
         */
        public void bindView(TagLayout rootView) {
            if (rootView == null) {
                throw new NullPointerException("未初始化TagFlowLayout");
            }
            this.rootView = rootView;
            isShowHighlight = rootView.isShowHighlight();
            itemDefaultDrawable = rootView.getItemDefaultDrawable();
            itemSelectDrawable = rootView.getItemSelectDrawable();
            itemDefaultTextColor = rootView.getItemDefaultTextColor();
            itemSelectTextColor = rootView.getItemSelectTextColor();
            maxSelection = rootView.getMaxSelection();
            mode = rootView.getMode();
        }

        /**
         * 设置标签组
         */
        public void addTags() {
            if (source == null || source.size() <= 0) return;
            rootView.removeAllViews();
            for (T item : source) {
                if (item == null) continue;
                final TagView<T> view = addTag(item);
                initSelectedViews((V) view);
                // 单个item的点击监控
                view.setListener(new TagWithListener<T>() {
                    @Override
                    public void onItemSelect(T item) {
                        if (mode == TagLayout.MODE_SINGLE_SELECT) {
                            if (isShowHighlight) view.selectItemChangeColorState();
                            singleSelectMode(item);
                        } else {
                            List<T> selectList = getSelectedList();
                            if ((maxSelection <= selectList.size() && maxSelection > 0) && !view.isItemSelected()) {
                                Toast.makeText(getContext(), "最多选择" + maxSelection + "个标签", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (isShowHighlight) view.selectItemChangeColorState();
                        }
                        if (onSubscribeListener != null) {
                            onSubscribeListener.onSubscribe(getSelectedList());
                        }
                    }
                });
                viewMap.put((V) view, item);
                rootView.addView(view);
            }
        }

        /**
         * 设置在初始化时所选中的View
         *
         * @param view
         */
        private void initSelectedViews(V view) {
            if (!isShowHighlight) return;
            if (selectItems == null || selectItems.size() <= 0) return;
            for (T select : selectItems) {
                if (checkIsItemNull(select)) continue;
                if (checkIsItemSame(view, select)) {
                    view.setItemSelected(true);
                    break;
                }
            }
        }

        /**
         * 单选操作模式
         */
        private void singleSelectMode(T item) {
            if (!isShowHighlight) return;
            for (TagView<T> view : viewMap.keySet()) {
                if (checkIsItemSame((V) view, item)) {
                    view.setItemSelected(true);
                } else {
                    view.setItemSelected(false);
                }
            }
        }


        /**
         * 刷新数据
         */
        public void notifyDataSetChanged() {
            addTags();
        }

        /**
         * 对于相同item的判断条件
         *
         * @param view
         * @param item
         * @return
         */
        protected abstract boolean checkIsItemSame(V view, T item);

        /**
         * 检查item是否是空指针
         *
         * @param item
         * @return
         */
        protected abstract boolean checkIsItemNull(T item);

        /**
         * 添加单个标签
         *
         * @param item
         * @return
         */
        protected abstract TagView<T> addTag(T item);

        /**
         * 获取所有item的数量
         */
        protected int getCount() {
            if (source == null) return 0;
            return source.size();
        }

        /**
         * 得到已选项目的列表
         *
         * @return
         */
        @SuppressWarnings("SuspiciousMethodCalls")
        public List<T> getSelectedList() {
            List<T> selectedList = new ArrayList<>();
            for (TagView<T> view : viewMap.keySet()) {
                if (view.isItemSelected()) {
                    T item = viewMap.get(view);
                    selectedList.add(item);
                }
            }
            return selectedList;
        }
    }

    public static class StringTagView extends TagView<String> {

        public StringTagView(Context context) {
            this(context, null);
        }

        public StringTagView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs, 0);
        }

        public StringTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setItem(String item) {
            super.setItem(item);
            textView.setText(item);
        }
    }

    public static class StringTagAdapter extends Adapter<StringTagView, String> {

        public StringTagAdapter(Context context, List<String> data) {
            this(context, data, null);
        }

        public StringTagAdapter(Context context, List<String> data, List<String> selectItems) {
            super(context, data, selectItems);
        }

        /**
         * 检查item和所选item是否一样
         *
         * @param view
         * @param item
         * @return
         */
        @Override
        protected boolean checkIsItemSame(StringTagView view, String item) {
            return TextUtils.equals(view.getItem(), item);
        }

        /**
         * 检查item是否是空指针
         *
         * @return
         */
        @Override
        protected boolean checkIsItemNull(String item) {
            return TextUtils.isEmpty(item);
        }

        /**
         * 添加标签
         *
         * @param item
         * @return
         */
        @Override
        protected StringTagView addTag(String item) {
            StringTagView tagView = new StringTagView(getContext());
            tagView.setPadding(20, 20, 20, 20);

            TextView textView = tagView.getTextView();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);

            tagView.setItemDefaultDrawable(itemDefaultDrawable);
            tagView.setItemSelectDrawable(itemSelectDrawable);
            tagView.setItemDefaultTextColor(itemDefaultTextColor);
            tagView.setItemSelectTextColor(itemSelectTextColor);
            tagView.setItem(item);
            return tagView;
        }
    }

}
