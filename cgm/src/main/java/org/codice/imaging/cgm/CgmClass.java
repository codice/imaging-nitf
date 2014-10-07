/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.cgm;

/**
 *
 */
public enum CgmClass {
    UNKNOWN(-1),
    DELIMITER(0),
    METAFILE_DESCRIPTOR(1),
    PICTURE_DESCRIPTOR(2),
    CONTROL(3),
    GRAPHICAL_PRIMITIVE(4),
    ATTRIBUTE(5),
    ESCAPE(6),
    EXTERNAL(7),
    SEGMENT(8),
    APPLICATION_STRUCTURE_DESCRIPTOR(9);

    static CgmClass lookup(int elementClass) {
        for (CgmClass cgmClass : values()) {
            if (cgmClass.getClassIdentifier() == elementClass) {
                return cgmClass;
            }
        }
        return UNKNOWN;
    }

	private CgmClass(final int classIdentifier) {
		id = classIdentifier;
	}

	// Internal state
	private final int id;

	public int getClassIdentifier() {
		return id;
	}
}
