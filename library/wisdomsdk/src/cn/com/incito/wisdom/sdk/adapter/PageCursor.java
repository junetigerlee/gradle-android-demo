package cn.com.incito.wisdom.sdk.adapter;

import java.util.ArrayList;
import java.util.List;

import android.database.AbstractCursor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;

/**
 * A convience class that lets you present an array of Cursors as a single
 * linear Cursor. The schema of the cursors presented is entirely up to the
 * creator of the MergeCursor, and may be different if that is desired. Calls to
 * getColumns, getColumnIndex, etc will return the value for the row that the
 * MergeCursor is currently pointing at.
 */
public class PageCursor extends AbstractCursor {
    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            // Reset our position so the optimizations in move-related code
            // don't screw us over
            mPos = -1;
        }

        @Override
        public void onInvalidated() {
            mPos = -1;
        }
    };

    public PageCursor(Cursor cursor) {
        mCursors = new ArrayList<Cursor>();
        if (cursor != null && !cursor.isClosed()) {
            mCursors.add(cursor);
            mCursor = cursor;

            for (int i = 0; i < mCursors.size(); i++) {
                mCursors.get(i).registerDataSetObserver(mObserver);
            }
        }
    }

    public void addCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            mCursors.add(cursor);
            if (mCursor == null) {
                mCursor = mCursors.get(0);
            }
            cursor.registerDataSetObserver(mObserver);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            count += mCursors.get(i).getCount();
        }
        return count;
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        /* Find the right cursor */
        mCursor = null;
        int cursorStartPos = 0;
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            if (newPosition < (cursorStartPos + mCursors.get(i).getCount())) {
                mCursor = mCursors.get(i);
                break;
            }

            cursorStartPos += mCursors.get(i).getCount();
        }

        /* Move it to the right position */
        if (mCursor != null) {
            boolean ret = mCursor.moveToPosition(newPosition - cursorStartPos);
            return ret;
        }
        return false;
    }

    @Override
    public String getString(int column) {
        return mCursor.getString(column);
    }

    @Override
    public short getShort(int column) {
        return mCursor.getShort(column);
    }

    @Override
    public int getInt(int column) {
        return mCursor.getInt(column);
    }

    @Override
    public long getLong(int column) {
        return mCursor.getLong(column);
    }

    @Override
    public float getFloat(int column) {
        return mCursor.getFloat(column);
    }

    @Override
    public double getDouble(int column) {
        return mCursor.getDouble(column);
    }

    @Override
    public int getType(int column) {
        return 0;
    }

    @Override
    public boolean isNull(int column) {
        return mCursor.isNull(column);
    }

    @Override
    public byte[] getBlob(int column) {
        return mCursor.getBlob(column);
    }

    @Override
    public String[] getColumnNames() {
        if (mCursor != null) {
            return mCursor.getColumnNames();
        } else {
            return new String[0];
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void deactivate() {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).deactivate();
        }
        super.deactivate();
    }

    @Override
    public void close() {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).close();
        }
        super.close();
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).registerContentObserver(observer);
        }
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).unregisterContentObserver(observer);
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {
            mCursors.get(i).unregisterDataSetObserver(observer);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean requery() {
        int length = mCursors.size();
        for (int i = 0; i < length; i++) {

            if (mCursors.get(i).requery() == false) {
                return false;
            }
        }

        return true;
    }

    private Cursor mCursor; // updated in onMove
    private List<Cursor> mCursors;
}
