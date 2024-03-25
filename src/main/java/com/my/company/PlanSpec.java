package com.my.company;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.deployment.Deployment;
import com.atlassian.bamboo.specs.api.builders.deployment.ReleaseNaming;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.notification.PlanFailedNotification;
import com.atlassian.bamboo.specs.builders.notification.ResponsibleRecipient;
import com.atlassian.bamboo.specs.builders.notification.WatchersRecipient;
import com.atlassian.bamboo.specs.builders.repository.git.GitRepository;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.deployment.Environment;



@BambooSpec
public class PlanSpec {

    private final String projectKey;
    private final String projectName;
    private final String planKey;
    private final String planName;
    private final String gitUrl;
    private final String branch;

    public PlanSpec(String projectKey, String projectName, String planKey, String planName, String gitUrl, String branch) {
        this.projectKey = projectKey;
        this.projectName = projectName;
        this.planKey = planKey;
        this.planName = planName;
        this.gitUrl = gitUrl;
        this.branch = branch;
    }



    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .key(new BambooKey(projectKey))
                .name(projectName),
                planName,
                new BambooKey(planKey))
                .planRepositories(
                        new GitRepository()
                                .name("Docker")
                                .url(gitUrl)
                                .branch(branch))
                .triggers(new RepositoryPollingTrigger())
                .notifications(new Notification()
                        .type(new PlanFailedNotification())
                        .recipients(new ResponsibleRecipient(),
                                new WatchersRecipient()));
        return plan;
    }

    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier(projectKey, planKey))
                .permissions(new Permissions()
                        .loggedInUserPermissions(PermissionType.VIEW)
                        .anonymousUserPermissionView());
        return planPermission;
    }

    private Deployment createDeployment() {
        return new Deployment(new PlanIdentifier(projectKey, planKey),
                    "Deployment project name")
                    .releaseNaming(new ReleaseNaming("release-1")
                            .autoIncrement(true))
                    .environments(new Environment("QA")
                            .tasks(new ScriptTask().inlineBody("echo Hello world!")))
                    .environments(new Environment("Staging")
                            .tasks(new ScriptTask().inlineBody("echo Hello world!")))
                    .environments(new Environment("Production")
                            .tasks(new ScriptTask().inlineBody("echo Hello world!")));
    }

    public static void main(String... argv) {
        BambooServer bambooServer = new BambooServer("http://localhost:8085");

        String projectName = "${projectName}";
        String projectKey = "${projectKey}";

        String planName = "${planName}";
        String planKey = "${planKey}";

        String gitUrl = "${gitUrl}";
        String branch = "${branch}";    

        PlanSpec planSpec = new PlanSpec(projectKey, projectName, planKey, planName, gitUrl, branch); 


        Plan plan = planSpec.plan();
        bambooServer.publish(plan);
    

        Deployment myDeployment = planSpec.createDeployment();
        bambooServer.publish(myDeployment);
    

        PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}
