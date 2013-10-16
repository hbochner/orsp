package edu.mit.broad.jira.issue;

import javax.annotation.Nullable;

import org.codehaus.jackson.map.annotate.JsonSerialize;


import edu.mit.broad.jira.customfields.CreateJiraIssueFieldsSerializer;

/**
 * We use a custom serializer here because custom fields are not
 * instance portable.  In other words, the custom field names in a cloned
 * dev instance of jira aren't the same as they are in production,
 * so there's a bit more work here to make sure that tickets
 * which have custom fields can be properly created in dev and prod.
 */
@JsonSerialize(using = CreateJiraIssueFieldsSerializer.class)
public class CreateFields extends UpdateFields {

    public static class Project {

        public Project() {

        }

        public Project(String key) {
            if (key == null) {
                throw new RuntimeException("key cannot be null");
            }
            this.key = key;
        }

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class Reporter {

        public Reporter() {
        }

        public Reporter(String name) {
            if (name == null) {
                throw new RuntimeException("name cannot be null");
            }

            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonSerialize(using = JsonLabopsJiraIssueTypeSerializer.class)
    public enum ProjectType {

        PICO_REQUEST("PicoGreen", "PICO"),
        GP_ARRAY_PROJECT("GP Array Plating Requests", "GAPREQ"),
        LCSET_PROJECT("Illumina Library Construction Tracking", "LCSET"),
        Product_Ordering("Product Ordering", "PDO"),
        Research_Projects("Research Projects", "RP");

        private final String projectName;
        private final String keyPrefix;

        private ProjectType(String projectNameIn, String keyPrefixIn) {
            projectName = projectNameIn;
            this.keyPrefix = keyPrefixIn;
        }

        public String getProjectName() {
            return projectName;
        }

        @SuppressWarnings("UnusedDeclaration")
        public String getKeyPrefix() {
            return keyPrefix;
        }
    }


    @JsonSerialize(using = JsonLabopsJiraIssueTypeSerializer.class)
    public enum IssueType {
        PICO_REQUEST("Pico Request"),
        FLUIDIGM_FP("Standard Fingerprint Request"),
        FLUIDIGM_DILUTION_FP("Dilution Fingerprint Request"),
        GAP_PLATING_REQUEST("GAP Plating Request"),
        WHOLE_EXOME_HYBSEL("Whole Exome (HybSel)"),
        EXOME_EXPRESS("Exome Express"),
        PRODUCT_ORDER("Product Order"),
        RESEARCH_PROJECT("Research Project"),
        ORSP_NHSR("ORSP NHSR request"),
        OTG("ORSP Test Ground");

        private final String jiraName;

        private IssueType(String jiraName) {
            this.jiraName = jiraName;
        }

        public String getJiraName() {
            return jiraName;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private Project project;

    private String summary;

    private String description;

    private IssueType issueType;

    private Reporter reporter;

    public Project getProject() {
        return project;
    }

    public Reporter getReporter() {
        return reporter;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setReporter(@Nullable Reporter reporter) {
        this.reporter = reporter;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public CreateFields() {
        project = new Project();
        reporter = new Reporter();
    }
}
