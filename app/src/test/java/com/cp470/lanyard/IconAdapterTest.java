package com.cp470.lanyard;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyInt;

public class IconAdapterTest {
    int[] mIcons = new int[]{1, 2, 5, 9, 10, 12};
    @Mock
    Context c = Mockito.mock(Context.class);

    public IconAdapter createIconAdapter() {
        IconAdapter adapter = new IconAdapter(c, mIcons);
        return adapter;
    }

    @Test
    public void canGetValues() {
        IconAdapter iconAdapter = createIconAdapter();
        assertEquals(iconAdapter.getCount(), mIcons.length);
        assertEquals(iconAdapter.getItem(2), mIcons[2]);
        assertEquals(iconAdapter.getItemId(3), 3);;
    }
}