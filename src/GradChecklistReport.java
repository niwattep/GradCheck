import java.io.PrintWriter;

public abstract class GradChecklistReport {
    static String[] humanityGenEdCourseIDs = { "2207363", "2207474", "2502393", "2541152",
        "2200301", "0123105", "0123101", "2200201", "2206101", "2542003", "2201111", "2207143",
        "2502430", "2541163", "2206247", "2502292", "2502291", "2541168", "2501292", "2501295",
        "0123104", "2541159", "3501456", "2502378", "2502379", "2542002", "2207389", "2207388",
        "2541151", "2223243", "3503111", "0123601", "2206248", "2542001", "2541157", "2722272", 
        "2722288", "2235320", "2210423", "2737110", "2210420", "2200330", "2210322", "2207361",
        "2200227", "3000103", "2210316", "2210313", "2200389", "2541162", "2210323", "2200183", 
        "2232253", "2200202", "2210315", "2200390", "2200394", "2209341", "2210216", "3503120",
        "2209373", "2541155", "2207201", "2236103", "2207165", "3502222", "2541158", "2210218", 
        "3502271", "2204184", "3500111", "2501191", "2501296", "2736106", "2541169", "0201105",
        "3501214", "3501120", "2200395", "2200392", "3501222", "2501298", "3501224", "2232241",
        "2200391", "2207203", "2210219", "2200222", "2207103", "2231255", "2541156", "2234482",
        "2210314", "2501297", "2244151", "2236204", "0201211", "2226001", "2210235", "2200185",
        "2210239"};
    static String[] scienceMathGenEdCourseIDs = { "3742102", "3742100", "3743422",
        "2110191", "2102041", "3600207", "2303150", "3600205", "3700104", "3700108", "3705103",
        "2313213", "2305151", "3640202", "2101251", "3141105", "3200103", "2111330", "3641201",
        "3700107", "3600206", "4000210", "3640203", "3900200", "4000205", "2305108", "2109336",
        "2110221", "3741207", "2307205", "2100311", "2313226", "2313221", "2111201", "2300150",
        "2307206", "3700114", "2112210", "2308200", "3307101", "2308303", "0201152", "3304102",
        "2300200", "2301170", "2305161", "3742106", "3741101", "2305103", "2309201", "2504101",
        "3306101", "2107220", "2302190", "2313222", "2107219", "3301102", "3600204", "2107221",
        "3643201", "3700109", "2310201", "3200109", "3600202", "2700110", "2312100", "3640201",
        "3700110", "3308100", "3310101", "3308101", "3213101", "2303165", "2314255", "3141213",
        "3010101", "3741102", "3309101", "3600208", "3700105", "3200110", "3705102", "210561",
        "2100111", "3309102", "2306416", "2308354", "2314257", "2305107", "2305109", "3700113"};
    static String[] interdisciplinaryGenEdCourseIDs = { "2503215", "0201200", "3000257",
        "3300100", "0201104", "0201153", "0201130", "0201122", "3301151", "0201119", "0201127",
        "0201125", "0201254", "0201251", "0201281", "0201118", "0201270", "0201131", "0201220",
        "3800252", "0201126", "2750178", "0201107", "0201101", "3000396", "0201207", "2502390",
        "0201108", "0201209", "3303100", "3018102", "3800309", "0201102", "0201103", "2305104",
        "0201233", "0201230", "0201111", "0201121", "0201256", "2209202", "3000281", "0201154",
        "0201231", "0201123", "2506504", "0201210", "2305100", "0201117", "3914101", "0201151",
        "0201201", "0201141", "0201202", "0201232", "0201203", "3303191", "0201204", "0201110",
        "0201205", "2503216", "0201206", "3213102", "0201120", "3305101", "0201109", "0201255",
        "0201234", "0201283", "2104181", "0201284", "0201129", "0201124", "0201252", "0201128",
        "2305106"};
    
    abstract public void printCurriculumCourses(PrintWriter writer, Student student);
    
    public void printReport (PrintWriter writer, Student student) {
        writer.println(student);
        writer.println();

        printCurriculumCourses(writer, student);
    }
}
