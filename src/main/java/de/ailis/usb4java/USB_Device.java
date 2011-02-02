/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import static de.ailis.usb4java.USB.usb_close;
import static de.ailis.usb4java.USB.usb_open;

import java.nio.ByteBuffer;


/**
 * USB Device.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Device
{
    /** The low-level device structure. */
    private final ByteBuffer device;


    /**
     * Constructor.
     *
     * @param device
     *            The low-level device structure.
     */

    private USB_Device(final ByteBuffer device)
    {
        this.device = device;
    }


    /**
     * Returns the filename.
     *
     * @return The filename.
     */

    public native String filename();


    /**
     * Returns the next USB device or null if none.
     *
     * @return The next USB device or null if none.
     */

    public native USB_Device next();


    /**
     * Returns the child devices.
     *
     * @return The child devices.
     */

    public native USB_Device[] children();


    /**
     * Returns the previous USB device or null if none.
     *
     * @return The previous USB device or null if none.
     */

    public native USB_Device prev();


    /**
     * Returns the device number. The original data type for this information is
     * an unsigned byte. This wrapper returns a short int instead to avoid
     * problems with values larger then 127.
     *
     * @return The device number (unsigned byte).
     */

    public native short devnum();


    /**
     * Returns the number of child devices. The original data type for this
     * information is an unsigned byte. This wrapper returns a short int instead
     * to avoid problems with values larger then 127.
     *
     * @return The number of child devices (unsigned byte).
     */

    public native short num_children();


    /**
     * Returns the USB bus.
     *
     * @return The USB bus.
     */

    public native USB_Bus bus();


    /**
     * Returns the USB device descriptor.
     *
     * @return The USB device descriptor.
     */

    public native USB_Device_Descriptor descriptor();


    /**
     * Returns the USB config descriptor.
     *
     * @return The USB config descriptor.
     */

    public native USB_Config_Descriptor[] config();


    /**
     * Dumps all device information to a string and returns it.
     *
     * @return The dumped device information.
     */

    public String dump()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("Device:%n"
            + "  filename %20s%n"
            + "  bus %25s%n"
            + "  num_children            %5d%n"
            + "  devnum                  %5d%n",
            filename(), bus().dirname(), num_children(), devnum()));
        final USB_Dev_Handle handle = usb_open(this);
        try
        {
            builder.append(descriptor().dump(handle).replaceAll("(?m)^",
                "  "));
            for (final USB_Config_Descriptor descriptor : config())
            {
                if (descriptor == null) continue;
                builder.append(descriptor.dump(handle).replaceAll("(?m)^",
                    "  "));
            }
            return builder.toString();
        }
        finally
        {
            if (handle != null) usb_close(handle);
        }
    }


    /**
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString()
    {
        return bus().dirname() + "/" + filename();
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        final USB_Device other = (USB_Device) obj;
        return bus().equals(other.bus()) && filename().equals(other.filename())
            && descriptor().equals(other.descriptor());
    }


    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + bus().hashCode();
        result = 37 * result + filename().hashCode();
        result = 37 * result + descriptor().hashCode();
        return result;
    }
}