package org.codice.nitf.metadatacomparison;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import java.util.Map;

import org.codice.imaging.nitf.core.AbstractNitfSegment;
import org.codice.imaging.nitf.core.DataExtensionSegmentNitfParseStrategy;
import org.codice.imaging.nitf.core.FileType;
import org.codice.imaging.nitf.core.ImageCoordinatePair;
import org.codice.imaging.nitf.core.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.Nitf;
import org.codice.imaging.nitf.core.NitfFileFactory;
import org.codice.imaging.nitf.core.NitfImageSegmentHeader;
import org.codice.imaging.nitf.core.RasterProductFormatUtilities;
import org.codice.imaging.nitf.core.RasterProductFormatAttributeParser;
import org.codice.imaging.nitf.core.RasterProductFormatAttributes;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;
import org.codice.imaging.nitf.core.Tre;
import org.codice.imaging.nitf.core.TreCollection;
import org.codice.imaging.nitf.core.TreEntry;
import org.codice.imaging.nitf.core.TreGroup;

public class FileComparer {
    static final String OUR_OUTPUT_EXTENSION = ".OURS.txt";
    static final String THEIR_OUTPUT_EXTENSION = ".THEIRS.txt";

    private String filename = null;
    private SlottedNitfParseStrategy parseStrategy  = null;
    private NitfImageSegmentHeader segment1 = null;
    private NitfDataExtensionSegmentHeader des1 = null;
    private BufferedWriter out = null;

    FileComparer(String fileName) {
        filename = fileName;
        generateGdalMetadata();
        generateOurMetadata();
        compareMetadataFiles();
    }


    private void generateOurMetadata() {
        parseStrategy = new DataExtensionSegmentNitfParseStrategy();
        try {
            NitfFileFactory.parse(new FileInputStream(filename), parseStrategy);
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!parseStrategy.getImageSegmentHeaders().isEmpty()) {
            segment1 = parseStrategy.getImageSegmentHeaders().get(0);
        }

        if (!parseStrategy.getDataExtensionSegmentHeaders().isEmpty()) {
            des1 = parseStrategy.getDataExtensionSegmentHeaders().get(0);
        }
        outputData();
    }

    private void outputData() {
        try {
            FileWriter fstream = new FileWriter(filename + OUR_OUTPUT_EXTENSION);
            out = new BufferedWriter(fstream);
            out.write("Driver: NITF/National Imagery Transmission Format\n");
            out.write("Files: " + filename + "\n");
            if (segment1 == null) {
                out.write(String.format("Size is 1, 1\n"));
            } else {
                out.write(String.format("Size is %d, %d\n", segment1.getNumberOfColumns(), segment1.getNumberOfRows()));
            }
            outputCoordinateSystem();

            outputBaseMetadata();

            outputTRExml();

            outputImageStructure();

            outputSubdatasets();

            outputRPCs();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void outputCoordinateSystem() throws IOException {
        boolean haveRPC = false;
        if (segment1 != null) {
            TreCollection treCollection = segment1.getTREsRawStructure();
            for (Tre tre : treCollection.getTREs()) {
                if (tre.getName().equals("RPC00B")) {
                    haveRPC = true;
                }
            }
        }
        if (segment1 == null) {
            out.write("Coordinate System is `'\n");
        } else if (segment1.getImageCoordinatesRepresentation() == ImageCoordinatesRepresentation.UTMUPSNORTH) {
            out.write("Coordinate System is:\n");
            out.write("PROJCS[\"unnamed\",\n");
            out.write("    GEOGCS[\"WGS 84\",\n");
            out.write("        DATUM[\"WGS_1984\",\n");
            out.write("            SPHEROID[\"WGS 84\",6378137,298.257223563,\n");
            out.write("                AUTHORITY[\"EPSG\",\"7030\"]],\n");
            out.write("            TOWGS84[0,0,0,0,0,0,0],\n");
            out.write("            AUTHORITY[\"EPSG\",\"6326\"]],\n");
            out.write("        PRIMEM[\"Greenwich\",0,\n");
            out.write("            AUTHORITY[\"EPSG\",\"8901\"]],\n");
            out.write("        UNIT[\"degree\",0.0174532925199433,\n");
            out.write("            AUTHORITY[\"EPSG\",\"9108\"]],\n");
            out.write("        AUTHORITY[\"EPSG\",\"4326\"]],\n");
            out.write("    PROJECTION[\"Transverse_Mercator\"],\n");
            out.write("    PARAMETER[\"latitude_of_origin\",-0],\n");
            out.write("    PARAMETER[\"central_meridian\",33],\n");
            out.write("    PARAMETER[\"scale_factor\",0.9996],\n");
            out.write("    PARAMETER[\"false_easting\",500000],\n");
            out.write("    PARAMETER[\"false_northing\",0]]\n");
        } else if (haveRPC || (segment1.getImageCoordinatesRepresentation() == ImageCoordinatesRepresentation.NONE)) {
            out.write("Coordinate System is `'\n");
        } else {
            out.write("Coordinate System is:\n");
            out.write("GEOGCS[\"WGS 84\",\n");
            out.write("    DATUM[\"WGS_1984\",\n");
            out.write("        SPHEROID[\"WGS 84\",6378137,298.257223563,\n");
            out.write("            AUTHORITY[\"EPSG\",\"7030\"]],\n");
            out.write("        TOWGS84[0,0,0,0,0,0,0],\n");
            out.write("        AUTHORITY[\"EPSG\",\"6326\"]],\n");
            out.write("    PRIMEM[\"Greenwich\",0,\n");
            out.write("        AUTHORITY[\"EPSG\",\"8901\"]],\n");
            out.write("    UNIT[\"degree\",0.0174532925199433,\n");
            out.write("        AUTHORITY[\"EPSG\",\"9108\"]],\n");
            out.write("    AUTHORITY[\"EPSG\",\"4326\"]]\n");
        }
    }

    private void outputBaseMetadata() throws IOException, ParseException {
        Map <String, String> metadata = new TreeMap<String, String>();

        addCommonFileLevelMetadata(metadata);
        Nitf nitf = parseStrategy.getNitfHeader();
        switch (nitf.getFileType()) {
            case NSIF_ONE_ZERO:
                metadata.put("NITF_FHDR", "NSIF01.00");
                break;
            case NITF_TWO_ZERO:
                metadata.put("NITF_FHDR", "NITF02.00");
                break;
            case NITF_TWO_ONE:
                metadata.put("NITF_FHDR", "NITF02.10");
                break;
        }
        if (nitf.getFileType() == FileType.NITF_TWO_ZERO) {
            addNITF20FileLevelMetadata(metadata);
        } else {
            addNITF21FileLevelMetadata(metadata);
        }

        TreCollection treCollection = nitf.getTREsRawStructure();
        addOldStyleMetadata(metadata, treCollection);
        if (segment1 != null) {
            addFirstImageSegmentMetadata(metadata);
        }

        if (des1 != null) {
            for (Tre tre : des1.getTREsRawStructure().getTREs()) {
                if ("RPFDES".equals(tre.getName())) {
                    outputRPFDESmetadata(metadata, tre);
                }
            }
        }

        out.write("Metadata:\n");
        for (String key : metadata.keySet()) {
            out.write(String.format("  %s=%s\n", key, metadata.get(key)));
        }
    }

    private void addCommonFileLevelMetadata(Map <String, String> metadata) throws IOException {
        Nitf nitf = parseStrategy.getNitfHeader();
        metadata.put("NITF_CLEVEL", String.format("%02d", nitf.getComplexityLevel()));
        metadata.put("NITF_ENCRYP", "0");
        metadata.put("NITF_FDT", nitf.getFileDateTime().getSourceString());
        metadata.put("NITF_FSCAUT", nitf.getFileSecurityMetadata().getClassificationAuthority());
        metadata.put("NITF_FSCLAS", nitf.getFileSecurityMetadata().getSecurityClassification().getTextEquivalent());
        metadata.put("NITF_FSCODE", nitf.getFileSecurityMetadata().getCodewords());
        metadata.put("NITF_FSCTLH", nitf.getFileSecurityMetadata().getControlAndHandling());
        metadata.put("NITF_FSCTLN", nitf.getFileSecurityMetadata().getSecurityControlNumber());
        metadata.put("NITF_FSREL", nitf.getFileSecurityMetadata().getReleaseInstructions());
        metadata.put("NITF_FSCOP", nitf.getFileSecurityMetadata().getFileCopyNumber());
        metadata.put("NITF_FSCPYS", nitf.getFileSecurityMetadata().getFileNumberOfCopies());
        metadata.put("NITF_FTITLE", nitf.getFileTitle());
        metadata.put("NITF_ONAME", nitf.getOriginatorsName());
        metadata.put("NITF_OPHONE", nitf.getOriginatorsPhoneNumber());
        metadata.put("NITF_OSTAID", nitf.getOriginatingStationId());
        metadata.put("NITF_STYPE", nitf.getStandardType());
    }

    private void addNITF20FileLevelMetadata(Map <String, String> metadata) throws IOException {
        Nitf nitf = parseStrategy.getNitfHeader();
        metadata.put("NITF_FSDWNG", nitf.getFileSecurityMetadata().getDowngradeDateOrSpecialCase().trim());
        if (nitf.getFileSecurityMetadata().getDowngradeEvent() != null) {
            metadata.put("NITF_FSDEVT", nitf.getFileSecurityMetadata().getDowngradeEvent());
        }
    }

    private void addNITF21FileLevelMetadata(Map <String, String> metadata) throws IOException {
        Nitf nitf = parseStrategy.getNitfHeader();
        metadata.put("NITF_FBKGC", (String.format("%3d,%3d,%3d",
                    (int)(nitf.getFileBackgroundColour().getRed() & 0xFF),
                    (int)(nitf.getFileBackgroundColour().getGreen() & 0xFF),
                    (int)(nitf.getFileBackgroundColour().getBlue() & 0xFF))));
        metadata.put("NITF_FSCATP", nitf.getFileSecurityMetadata().getClassificationAuthorityType());
        metadata.put("NITF_FSCLSY", nitf.getFileSecurityMetadata().getSecurityClassificationSystem());
        metadata.put("NITF_FSCLTX", nitf.getFileSecurityMetadata().getClassificationText());
        metadata.put("NITF_FSCRSN", nitf.getFileSecurityMetadata().getClassificationReason());
        metadata.put("NITF_FSDCDT", nitf.getFileSecurityMetadata().getDeclassificationDate());
        metadata.put("NITF_FSDCTP", nitf.getFileSecurityMetadata().getDeclassificationType());
        if (nitf.getFileSecurityMetadata().getDeclassificationExemption().length() > 0) {
            metadata.put("NITF_FSDCXM", String.format("%4s", nitf.getFileSecurityMetadata().getDeclassificationExemption()));
        } else {
            metadata.put("NITF_FSDCXM", "");
        }
        metadata.put("NITF_FSDG", nitf.getFileSecurityMetadata().getDowngrade());
        metadata.put("NITF_FSDGDT", nitf.getFileSecurityMetadata().getDowngradeDate());
        metadata.put("NITF_FSSRDT", nitf.getFileSecurityMetadata().getSecuritySourceDate());
    }

    private void addFirstImageSegmentMetadata(Map <String, String> metadata) throws IOException, ParseException {

        addCommonImageSegmentMetadata(metadata);
        Nitf nitf = parseStrategy.getNitfHeader();

        if (nitf.getFileType() == FileType.NITF_TWO_ZERO) {
            addNITF20ImageSegmentMetadata(metadata);
        } else {
            addNITF21ImageSegmentMetadata(metadata);
        }
        addRpfNamesMetadata(metadata);

        addOldStyleMetadata(metadata, segment1.getTREsRawStructure());
    }

    private void addNITF20ImageSegmentMetadata(Map <String, String> metadata) throws IOException {
        Nitf nitf = parseStrategy.getNitfHeader();
        metadata.put("NITF_ICORDS", segment1.getImageCoordinatesRepresentation().getTextEquivalent(nitf.getFileType()));
        metadata.put("NITF_ITITLE", segment1.getImageIdentifier2());
        metadata.put("NITF_ISDWNG", segment1.getSecurityMetadata().getDowngradeDateOrSpecialCase().trim());
        if (segment1.getSecurityMetadata().getDowngradeEvent() != null) {
            metadata.put("NITF_ISDEVT", segment1.getSecurityMetadata().getDowngradeEvent());
        }
    }

    private void addNITF21ImageSegmentMetadata(Map <String, String> metadata) throws IOException {
        Nitf nitf = parseStrategy.getNitfHeader();
        if (segment1.getImageCoordinatesRepresentation() == ImageCoordinatesRepresentation.NONE) {
            metadata.put("NITF_ICORDS", "");
        } else {
            metadata.put("NITF_ICORDS", segment1.getImageCoordinatesRepresentation().getTextEquivalent(nitf.getFileType()));
        }
        metadata.put("NITF_IID2", segment1.getImageIdentifier2());
        metadata.put("NITF_ISCATP", segment1.getSecurityMetadata().getClassificationAuthorityType());
        metadata.put("NITF_ISCLSY", segment1.getSecurityMetadata().getSecurityClassificationSystem());
        metadata.put("NITF_ISCLTX", segment1.getSecurityMetadata().getClassificationText());
        metadata.put("NITF_ISDCDT", segment1.getSecurityMetadata().getDeclassificationDate());
        metadata.put("NITF_ISDCTP", segment1.getSecurityMetadata().getDeclassificationType());
        metadata.put("NITF_ISCRSN", segment1.getSecurityMetadata().getClassificationReason());
        if ((segment1.getSecurityMetadata().getDeclassificationExemption() != null)
            && (segment1.getSecurityMetadata().getDeclassificationExemption().length() > 0)) {
            metadata.put("NITF_ISDCXM", String.format("%4s", segment1.getSecurityMetadata().getDeclassificationExemption()));
        } else {
            metadata.put("NITF_ISDCXM", "");
        }
        metadata.put("NITF_ISDG", segment1.getSecurityMetadata().getDowngrade());
        metadata.put("NITF_ISDGDT", segment1.getSecurityMetadata().getDowngradeDate());
        metadata.put("NITF_ISSRDT", segment1.getSecurityMetadata().getSecuritySourceDate());
    }

    private void addCommonImageSegmentMetadata(Map <String, String> metadata) throws IOException {
        metadata.put("NITF_ABPP", String.format("%02d", segment1.getActualBitsPerPixelPerBand()));
        metadata.put("NITF_CCS_COLUMN", String.format("%d", segment1.getImageLocationColumn()));
        metadata.put("NITF_CCS_ROW", String.format("%d", segment1.getImageLocationRow()));
        metadata.put("NITF_IALVL", String.format("%d", segment1.getAttachmentLevel()));
        metadata.put("NITF_IC", segment1.getImageCompression().getTextEquivalent());
        metadata.put("NITF_ICAT", segment1.getImageCategory().getTextEquivalent());
        String idatim = rightTrimToLetterOrDigit(segment1.getImageDateTime().getSourceString());
        if (idatim.length() > 0) {
            metadata.put("NITF_IDATIM", idatim);
        } else {
            metadata.put("NITF_IDATIM", " ");
        }
        metadata.put("NITF_IDLVL", String.format("%d", segment1.getImageDisplayLevel()));
        if (segment1.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE) {
            metadata.put("NITF_IGEOLO", String.format("%s%s%s%s",
                                                    segment1.getImageCoordinates().getCoordinate00().getSourceFormat(),
                                                    segment1.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat(),
                                                    segment1.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat(),
                                                    segment1.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat()));
        }
        metadata.put("NITF_IID1", segment1.getIdentifier());
        metadata.put("NITF_ILOC_COLUMN", String.format("%d", segment1.getImageLocationColumn()));
        metadata.put("NITF_ILOC_ROW", String.format("%d", segment1.getImageLocationRow()));
        metadata.put("NITF_IMAG", segment1.getImageMagnification());
        metadata.put("NITF_IMODE", segment1.getImageMode().getTextEquivalent());
        metadata.put("NITF_IREP", segment1.getImageRepresentation().getTextEquivalent());
        metadata.put("NITF_ISCAUT", segment1.getSecurityMetadata().getClassificationAuthority());
        metadata.put("NITF_ISCLAS", segment1.getSecurityMetadata().getSecurityClassification().getTextEquivalent());
        metadata.put("NITF_ISCODE", segment1.getSecurityMetadata().getCodewords());
        metadata.put("NITF_ISCTLH", segment1.getSecurityMetadata().getControlAndHandling());
        metadata.put("NITF_ISCTLN", segment1.getSecurityMetadata().getSecurityControlNumber());
        if (!segment1.getImageComments().isEmpty()) {
            StringBuilder commentBuilder = new StringBuilder();
            for (String imageComment : segment1.getImageComments()) {
                commentBuilder.append(String.format("%-80s", imageComment));
            }
            metadata.put("NITF_IMAGE_COMMENTS", commentBuilder.toString());
        }
        metadata.put("NITF_ISORCE", segment1.getImageSource());
        metadata.put("NITF_ISREL", segment1.getSecurityMetadata().getReleaseInstructions());
        metadata.put("NITF_PJUST", segment1.getPixelJustification().getTextEquivalent());
        metadata.put("NITF_PVTYPE", segment1.getPixelValueType().getTextEquivalent());
        if (segment1.getImageTargetId().toString().length() > 0) {
            metadata.put("NITF_TGTID", rightTrim(segment1.getImageTargetId().toString()));
        } else {
            metadata.put("NITF_TGTID", "");
        }
    }

    private void addRpfNamesMetadata(Map <String, String> metadata) throws IOException, ParseException {
        if (filename.toLowerCase().endsWith(".ntf")) {
            // GDAL does this off the filename, not off the IID2, so it won't show these for "plain" NITF files
            return;
        }
        RasterProductFormatUtilities rpfUtils = new RasterProductFormatUtilities();

        String rpfAbbreviation = rpfUtils.getAbbreviationForFileName(segment1.getImageIdentifier2());
        if (rpfAbbreviation != null) {
            metadata.put("NITF_SERIES_ABBREVIATION", rpfAbbreviation);
        }
        String rpfName = rpfUtils.getNameForFileName(segment1.getImageIdentifier2());
        if (rpfName != null) {
            if ("Joint Operations Graphic - Air".equals(rpfName)) {
                metadata.put("NITF_SERIES_NAME", "Joint Operation Graphic - Air");
            } else {
                metadata.put("NITF_SERIES_NAME", rpfName);
            }
        }
    }

    private void outputImageStructure() throws IOException {
        if (segment1 != null) {
            switch (segment1.getImageCompression()) {
                case JPEG:
                case JPEGMASK:
                    out.write("Image Structure Metadata:\n");
                    out.write("  COMPRESSION=JPEG\n");
                    break;
                case BILEVEL:
                case BILEVELMASK:
                case DOWNSAMPLEDJPEG:
                    out.write("Image Structure Metadata:\n");
                    out.write("  COMPRESSION=BILEVEL\n");
                    break;
                case LOSSLESSJPEG:
                    out.write("Image Structure Metadata:\n");
                    out.write("  COMPRESSION=LOSSLESS JPEG\n");
                    break;
                case JPEG2000:
                case JPEG2000MASK:
                    out.write("Image Structure Metadata:\n");
                    out.write("  COMPRESSION=JPEG2000\n");
                    break;
                case VECTORQUANTIZATION:
                case VECTORQUANTIZATIONMASK:
                    out.write("Image Structure Metadata:\n");
                    out.write("  COMPRESSION=VECTOR QUANTIZATION\n");
                    break;
            }
        }
    }

    private void outputSubdatasets() throws IOException {
        if (parseStrategy.getImageSegmentHeaders().size() > 1) {
            out.write("Subdatasets:\n");
            for (int i = 0; i < parseStrategy.getImageSegmentHeaders().size(); ++i) {
                out.write(String.format("  SUBDATASET_%d_NAME=NITF_IM:%d:%s\n", i+1, i, filename));
                out.write(String.format("  SUBDATASET_%d_DESC=Image %d of %s\n", i+1, i+1, filename));
            }
        }
    }

    private void outputTRExml() throws IOException {
        if (shouldOutputTREs()) {
            out.write("Metadata (xml:TRE):\n");
            out.write("<tres>\n");
            outputTresForSegment(parseStrategy.getNitfHeader(), "file");
            if (segment1 != null) {
                outputTresForSegment(segment1, "image");
            }
            if (des1 != null) {
                outputTresForSegment(des1, "des TRE_OVERFLOW");
            }
            out.write("</tres>\n\n");
        }
    }

    private boolean shouldOutputTREs() {
        return shouldOutputFileTREs() || shouldOutputImageTREs() || shouldOutputDESTREs();
    }

    private boolean shouldOutputFileTREs() {
        return hasTREsOtherThanRPF(parseStrategy.getNitfHeader().getTREsRawStructure());
    }

    private boolean shouldOutputImageTREs() {
        return (segment1 != null) && (hasTREsOtherThanRPF(segment1.getTREsRawStructure()));
    }

    private boolean shouldOutputDESTREs() {
        return (des1 != null) && (hasTREsOtherThanRPF(des1.getTREsRawStructure()));
    }

    private void outputTresForSegment(AbstractNitfSegment segment, String label) throws IOException {
        TreCollection treCollection = segment.getTREsRawStructure();
        for (Tre tre : treCollection.getTREs()) {
            if (tre.getRawData() == null) {
                // We parsed this TRE
                outputThisTre(out, tre, label);
            }
        }
    }

    private boolean hasTREsOtherThanRPF(TreCollection treCollection) {
        int treCountNonRPF = 0;
        for (Tre tre : treCollection.getTREs()) {
            if ((tre.getName().equals("RPFHDR")) || (tre.getName().equals("RPFIMG")) || (tre.getName().equals("RPFDES"))) {
                continue;
            }
            if (tre.getRawData() != null) {
                //  We have raw data for this TRE, so this is not a TRE we recognised
                continue;
            }
            treCountNonRPF++;
        }
        return (treCountNonRPF != 0);
    }

    private void outputRPCs() throws IOException {
        Map <String, String> rpc = new TreeMap<String, String>();
        if (segment1 != null) {
            // Walk the segment1 TRE collection and add RPC entries here
            TreCollection treCollection = segment1.getTREsRawStructure();
            for (Tre tre : treCollection.getTREs()) {
                if (tre.getName().equals("RPC00B")) {
                    for (TreEntry entry : tre.getEntries()) {
                        if (entry.getName().equals("SUCCESS")) {
                            continue;
                        }
                        if (entry.getName().equals("ERR_BIAS") || entry.getName().equals("ERR_RAND")) {
                            continue;
                        }
                        if (entry.getFieldValue() != null) {
                            if (entry.getName().equals("LONG_OFF") || entry.getName().equals("LONG_SCALE") || entry.getName().equals("LAT_OFF") || entry.getName().equals("LAT_SCALE")) {
                                // skip this, we're filtering it out for both cases
                            } else {
                                Integer rpcValue = Integer.parseInt(entry.getFieldValue());
                                rpc.put(entry.getName(), rpcValue.toString());
                            }
                        } else if ((entry.getGroups() != null) && (!entry.getGroups().isEmpty())) {
                            StringBuilder builder = new StringBuilder();
                            for (TreGroup group : entry.getGroups()) {
                                for (TreEntry groupEntry : group.getEntries()) {
                                    try {
                                        double fieldVal = Double.parseDouble(groupEntry.getFieldValue());
                                        builder.append(cleanupNumberString(fieldVal));
                                        builder.append(" ");
                                    } catch (NumberFormatException e) {
                                        builder.append(String.format("%s ", groupEntry.getFieldValue()));
                                    }
                                }
                            }
                            // These are too sensitive to number formatting issues, and we're already checking
                            // the value in the real TRE.
                            // rpc.put(entry.getName(), builder.toString());
                        }
                    }
                    try {
                        double longOff = Double.parseDouble(tre.getFieldValue("LONG_OFF"));
                        double longScale = Double.parseDouble(tre.getFieldValue("LONG_SCALE"));
                        double longMin = longOff - (longScale / 2.0);
                        double longMax = longOff + (longScale / 2.0);
                        rpc.put("MAX_LONG", cleanupNumberString(longMax));
                        rpc.put("MIN_LONG", cleanupNumberString(longMin));
                        double latOff = Double.parseDouble(tre.getFieldValue("LAT_OFF"));
                        double latScale = Double.parseDouble(tre.getFieldValue("LAT_SCALE"));
                        double latMin = latOff - (latScale / 2.0);
                        double latMax = latOff + (latScale / 2.0);
                        rpc.put("MAX_LAT", cleanupNumberString(latMax));
                        rpc.put("MIN_LAT", cleanupNumberString(latMin));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!rpc.keySet().isEmpty()) {
            out.write("RPC Metadata:\n");
            for (String tagname : rpc.keySet()) {
                out.write(String.format("  %s=%s\n", tagname, rpc.get(tagname)));
            }
        }
    }

    private static String cleanupNumberString(double fieldVal) {
        if (fieldVal == (int)fieldVal) {
            return String.format("%d", (int)fieldVal);
        }
        String naiveString = String.format("%.12g", fieldVal);
        if (naiveString.contains("e-")) {
            return naiveString.replaceAll("\\.?0*e", "e");
        } else if (naiveString.contains(".")) {
            return naiveString.replaceAll("\\.?0*$", "");
        }
        return naiveString;
    }

    private void addOldStyleMetadata(Map <String, String> metadata, TreCollection treCollection) {
        for (Tre tre : treCollection.getTREs()) {
            if (tre.getPrefix() != null) {
                // if it has a prefix, its probably an old-style NITF metadata field
                List<TreEntry> entries = tre.getEntries();
                for (TreEntry entry: entries) {
                    metadata.put(tre.getPrefix() + entry.getName(), rightTrim(entry.getFieldValue()));
                }
            } else if ("ICHIPB".equals(tre.getName())) {
                outputICHIPmetadata(metadata, tre);
            }
        }
    }

    private void outputICHIPmetadata(Map <String, String> metadata, Tre tre) {
        List<TreEntry> entries = tre.getEntries();
        for (TreEntry entry: entries) {
            if ("XFRM_FLAG".equals(entry.getName())) {
                // GDAL skips this one
                continue;
            }
            BigDecimal value = new BigDecimal(entry.getFieldValue().trim()).stripTrailingZeros();
            if ("ANAMRPH_CORR".equals(entry.getName())) {
                // Special case for GDAL
                metadata.put("ICHIP_ANAMORPH_CORR", value.toPlainString());
            } else {
                metadata.put("ICHIP_" + entry.getName(), value.toPlainString());
            }
        }
    }

    private void outputRPFDESmetadata(Map <String, String> metadata, Tre tre) {
        try {
            byte[] rawRpfDesData = tre.getRawData();
            RasterProductFormatAttributes attributes = new RasterProductFormatAttributeParser().parseRpfDes(rawRpfDesData);
            if (attributes.getCurrencyDate() != null) {
                metadata.put("NITF_RPF_CurrencyDate", attributes.getCurrencyDate());
            } else {
                Set<Integer> dateIndices = attributes.getCurrencyDateArealReferences();
                if (!dateIndices.isEmpty()) {
                    metadata.put("NITF_RPF_CurrencyDate", attributes.getCurrencyDate(dateIndices.iterator().next()));
                }
            }
            if (attributes.getProductionDate() != null) {
                metadata.put("NITF_RPF_ProductionDate", attributes.getProductionDate());
            } else {
                Set<Integer> dateIndices = attributes.getProductionDateArealReferences();
                if (!dateIndices.isEmpty()) {
                    metadata.put("NITF_RPF_ProductionDate", attributes.getProductionDate(dateIndices.iterator().next()));
                }
            }
            if (attributes.getSignificantDate() != null) {
                metadata.put("NITF_RPF_SignificantDate", attributes.getSignificantDate());
            } else {
                Set<Integer> dateIndices = attributes.getSignificantDateArealReferences();
                if (!dateIndices.isEmpty()) {
                    metadata.put("NITF_RPF_SignificantDate", attributes.getSignificantDate(dateIndices.iterator().next()));
                }
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void outputThisTre(BufferedWriter out, Tre tre, String location) throws IOException {
        doIndent(out, 1);
        out.write("<tre name=\"" + tre.getName().trim() + "\" location=\"" + location + "\">\n");
        for (TreEntry entry : tre.getEntries()) {
            outputThisEntry(out, entry, 2);
        }
        doIndent(out, 1);
        out.write("</tre>\n");
    }

    private static void outputThisEntry(BufferedWriter out, TreEntry entry, int indentLevel) throws IOException {
        if (entry.getFieldValue() != null) {
            doIndent(out, indentLevel);
            out.write("<field name=\"" + entry.getName() + "\" value=\"" + rightTrim(entry.getFieldValue()) + "\" />\n");
        }
        if ((entry.getGroups() != null) && (!entry.getGroups().isEmpty())) {
            doIndent(out, indentLevel);
            out.write("<repeated name=\"" + entry.getName() + "\" number=\"" + entry.getGroups().size() + "\">\n");
            int i = 0;
            for (TreGroup group : entry.getGroups()) {
                doIndent(out, indentLevel + 1);
                out.write(String.format("<group index=\"%d\">\n", i));
                for (TreEntry groupEntry : group.getEntries()) {
                    outputThisEntry(out, groupEntry, indentLevel + 2);
                }
                doIndent(out, indentLevel + 1);
                out.write(String.format("</group>\n"));
                i = i + 1;
            }
            doIndent(out, indentLevel);
            out.write("</repeated>\n");
        }
    }

    private static String rightTrim(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    private static String rightTrimToLetterOrDigit(final String s) {
        int i = s.length() - 1;
        while ((i >= 0) && !Character.isLetterOrDigit(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    private static void doIndent(BufferedWriter out, int indentLevel) throws IOException {
        for (int i = 0; i < indentLevel; ++i) {
            out.write("  ");
        }
    }

    // This is ugly - feel free to fix it any time.
    private static String makeGeoString(ImageCoordinatePair coords) {
        double latitude = coords.getLatitude();
        double longitude = coords.getLongitude();

        String northSouth = "N";
        if (latitude < 0.0) {
            northSouth = "S";
            latitude = Math.abs(latitude);
        }
        String eastWest = "E";
        if (longitude < 0.0) {
            eastWest = "W";
            longitude = Math.abs(longitude);
        }

        int latDegrees = (int)Math.floor(latitude);
        double minutesAndSecondsPart = (latitude -latDegrees) * 60;
        int latMinutes = (int)Math.floor(minutesAndSecondsPart);
        double secondsPart = (minutesAndSecondsPart - latMinutes) * 60;
        int latSeconds = (int)Math.round(secondsPart);
        if (latSeconds == 60) {
            latMinutes++;
            latSeconds = 0;
        }
        if (latMinutes == 60) {
            latDegrees++;
            latMinutes = 0;
        }
        int lonDegrees = (int)Math.floor(longitude);
        minutesAndSecondsPart = (longitude - lonDegrees) * 60;
        int lonMinutes = (int)Math.floor(minutesAndSecondsPart);
        secondsPart = (minutesAndSecondsPart - lonMinutes) * 60;
        int lonSeconds = (int)Math.round(secondsPart);
        if (lonSeconds == 60) {
            lonMinutes++;
            lonSeconds = 0;
        }
        if (lonMinutes == 60) {
            lonDegrees++;
            lonMinutes = 0;
        }
        return String.format("%02d%02d%02d%s%03d%02d%02d%s", latDegrees, latMinutes, latSeconds, northSouth, lonDegrees, lonMinutes, lonSeconds, eastWest);
    }

    private void generateGdalMetadata() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("gdalinfo", "-nogcp", "-mdd", "xml:TRE", filename);
            processBuilder.environment().put("NITF_OPEN_UNDERLYING_DS", "NO");
            Process process = processBuilder.start();
            BufferedWriter out = null;
            try {
                FileWriter fstream = new FileWriter(filename + THEIR_OUTPUT_EXTENSION);
                out = new BufferedWriter(fstream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader infoOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"), 1000000);
            boolean done = false;
            try {
                do {
                    do {
                        String line = infoOutputReader.readLine();
                        if (line == null) {
                            done = true;
                            break;
                        }
                        if (line.startsWith("Origin = (")) {
                            // System.out.println("Filtering on Origin");
                            continue;
                        }
                        if (line.startsWith("Pixel Size = (")) {
                            // System.out.println("Filtering on Pixel Size");
                            continue;
                        }
                        if (line.startsWith("  LINE_DEN_COEFF=") || line.startsWith("  LINE_NUM_COEFF=") || line.startsWith("  SAMP_DEN_COEFF=") || line.startsWith("  SAMP_NUM_COEFF=")) {
                            // System.out.println("Filtering out RPC coefficients");
                            continue;
                        }
                        if (line.startsWith("  LAT_SCALE=") || line.startsWith("  LONG_SCALE=") || line.startsWith("  LAT_OFF=") || line.startsWith("  LONG_OFF=")) {
                            // System.out.println("Filtering out RPC coefficients");
                            continue;
                        }
                        if (line.startsWith("Corner Coordinates:")) {
                            // System.out.println("Exiting on Corner Coordinates");
                            done = true;
                            break;
                        }
                        if (line.startsWith("Band 1 Block=")) {
                            // System.out.println("Exiting on Band 1 Block");
                            done = true;
                            break;
                        }
                        if (out != null) {
                            out.write(line + "\n");
                        }
                    } while (infoOutputReader.ready() && (!done));
                    Thread.sleep(100);
                } while (!done);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (out != null) {
                out.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void compareMetadataFiles() {
        List<String> theirs = fileToLines(filename + THEIR_OUTPUT_EXTENSION);
        List<String> ours  = fileToLines(filename + OUR_OUTPUT_EXTENSION);

        Patch<String> patch = DiffUtils.diff(theirs, ours);

        if (!patch.getDeltas().isEmpty()) {
            for (Delta<String> delta: patch.getDeltas()) {
                    System.out.println(delta);
            }
            System.out.println("  * Done");
        } else {
            new File(filename + THEIR_OUTPUT_EXTENSION).delete();
            new File(filename + OUR_OUTPUT_EXTENSION).delete();
        }
    }

    private static List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
                BufferedReader in = new BufferedReader(new FileReader(filename));
                while ((line = in.readLine()) != null) {
                        lines.add(line);
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return lines;
    }

}