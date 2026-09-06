package dev.zehen.myinsta2.extension;

import android.R;
import kotlin.jvm.functions.Function0;
import LX.LAb;
import LX.LAc;
import LX.LKA;
import LX.Vfc;

/** Instagram 445 comment action button used by the exact comment menu hook. */
@SuppressWarnings("unused")
public final class CopyTextButton extends LKA implements Vfc {
    public static final CopyTextButton A00 = new CopyTextButton();

    private CopyTextButton() {
        super(
                LKA.A00,
                new LAb(0x7f082230),
                new LAc(R.string.copy),
                new Function0<Object>() {
                    @Override
                    public Object invoke() {
                        return null;
                    }
                }
        );
    }
}
