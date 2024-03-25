// package com.my.company;

// import com.atlassian.bamboo.specs.api.BambooSpec;
// import com.atlassian.bamboo.specs.api.builders.plan.Plan;
// import com.atlassian.bamboo.specs.api.builders.project.Project;
// import com.atlassian.bamboo.specs.util.BambooServer;
// import com.atlassian.bamboo.specs.api.builders.BambooKey;
// import com.atlassian.bamboo.specs.api.builders.docker.DockerConfiguration;
// // import com.atlassian.bamboo.specs.api.builders.BambooKey;
// // import com.atlassian.bamboo.specs.api.builders.credentials.SharedCredentialsIdentifier;
// // import com.atlassian.bamboo.specs.api.builders.docker.DockerConfiguration;
// import com.atlassian.bamboo.specs.api.builders.plan.Job;
// import com.atlassian.bamboo.specs.api.builders.plan.Stage;
// // import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
// // import com.atlassian.bamboo.specs.api.builders.repository.VcsRepository;
// import com.atlassian.bamboo.specs.builders.repository.git.GitRepository;
// import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
// import com.atlassian.bamboo.specs.builders.task.InjectVariablesTask;
// import com.atlassian.bamboo.specs.builders.task.ScriptTask;
// import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
// import com.atlassian.bamboo.specs.model.task.InjectVariablesScope;

// import java.util.Scanner;
// // import com.atlassian.bamboo.specs.model.task.InjectVariablesScope;

// /**
//  * Plan configuration for Bamboo.
//  * Learn more on: <a href=
//  * "https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs</a>
//  */
// @BambooSpec
// public class PlanSpec {

//         public static void main(final String[] args) throws Exception {
//                 // By default, credentials are read from the '.credentials' file.
//                 BambooServer bambooServer = new BambooServer("http://188.166.225.122:8085/");
                // try (Scanner myObj = new Scanner(System.in)) {
                //         System.out.println("Enter Project Name");
                //         String projectName = myObj.nextLine();
                //         String projectKey = projectName.substring(0, Math.min(projectName.length(), 3)).toUpperCase();
                //         System.out.println("Project Key: " + projectKey);
                //         System.out.println("Enter Plan Name");
                //         String planName = myObj.nextLine();
                //         String planKey = planName.substring(0, Math.min(planName.length(), 3)).toUpperCase();
                //         System.out.println("Plan Key: " + planKey);
                        // System.out.println("Enter Git URL");
                        // String gitUrl = myObj.nextLine();
                        // System.out.println("Select Branch");
//                         String branch = myObj.nextLine();
//                         Plan plan = new PlanSpec().createPlan(projectName, projectKey, planName, planKey, gitUrl,
//                                         branch);

//                         bambooServer.publish(plan);
                        
//                 }

//         }

//         // Define project name and key
//         // String projectName = "Project Dong";
//         // String projectKey = "PDD";
//         // String planName = "Test Ker";
//         // String planKey = "TDD";

//         Project project(String projectName, String projectKey) {
//                 return new Project()
//                                 .name(projectName)
//                                 .key(projectKey);
//         }

//         public Plan createPlan(String projectName, String projectKey, String planName, String planKey, String gitUrl,
//                         String branch) {
//                 return new Plan(
//                                 project(projectName, projectKey),
//                                 planName, planKey)
//                                 .description("Plan created from (enter repository url of your plan)")
//                                 .stages(new Stage("Scan Stage")
//                                                 .jobs(new Job("Scan Job",
//                                                                 new BambooKey("JOB1"))
//                                                                 .tasks(new VcsCheckoutTask()
//                                                                                 .description("Checkout Default Repository")
//                                                                                 .checkoutItems(new CheckoutItem()
//                                                                                                 .defaultRepository())
//                                                                                 .cleanCheckout(true),
//                                                                                 new ScriptTask()
//                                                                                                 .description("Sonar Scan")
//                                                                                                 .inlineBody("sonar-scanner -Dsonar.host.url=/ -Dsonar.login=\"squ_1da\" -Dsonar.branch.name=\"${bamboo.planRepository.1.branchName}\" -Dsonar.qualitygate.wait=false -Dproject.settings=sonar-project.properties"))
//                                                                 .dockerConfiguration(new DockerConfiguration()
//                                                                                 .enabled(false))),

//                                                 new Stage("Build Images")
//                                                                 .jobs(new Job("Build Image",
//                                                                                 new BambooKey("BI"))
//                                                                                 .tasks(new VcsCheckoutTask()
//                                                                                                 .description("Checkout Default Repository")
//                                                                                                 .checkoutItems(new CheckoutItem()
//                                                                                                                 .defaultRepository())
//                                                                                                 .cleanCheckout(true),
//                                                                                                 new ScriptTask()
//                                                                                                                 .description("Version Increment")
//                                                                                                                 .inlineBody("echo \"test\"\nversion=$(head -1 version)\npart1=$(cut -d '.' -f1 <<<\"$version\")\npart2=$(cut -d '.' -f2 <<<\"$version\")\npart3=$(cut -d '.' -f3 <<<\"$version\")\nstage=$(cut -d '-' -f2 <<<\"$part3\")\npart3=$(cut -d '-' -f1 <<<\"$part3\")\n\nif [ \"${bamboo.env}\" = \"-stagging\" ]\nthen\n    version1=\"$part1.$part2.$part3-beta\"\nelif [ \"${bamboo.env}\" = \"-dc\" ]\nthen\n    version1=\"$part1.$part2.$part3-stable\"\nelse\n    version1=\"$part1.$part2.$part3-alpha\"\nfi\n\nsed -i \"s/$version/$version1/\" version"),
//                                                                                                 new InjectVariablesTask()
//                                                                                                                 .description("Variable Version")
//                                                                                                                 .path("version")
//                                                                                                                 .namespace("ndsaatrmq")
//                                                                                                                 .scope(InjectVariablesScope.RESULT),
//                                                                                                 new ScriptTask()
//                                                                                                                 .description("Image Processing")
//                                                                                                                 .inlineBody("reg_url=/nds-images\n\ndocker login - \"\"  \n\n/opt/oc login ${bamboo.ocp_url_dev} -u ${bamboo.ocp_artifactory_username} -p${bamboo.ocp_password_dev} --insecure-skip-tls-verify\n/opt/oc project ${bamboo.projectName}\n\ndocker login -u ${bamboo.ocp_artifactory_username} -p $(/opt/oc whoami -t) ${bamboo.artifactory_url_dev}\n\ndocker build --build-arg bitbucketUser=${bamboo.bitbucket_user} --build-arg bitbucketToken=${bamboo.bitbucket_token} --build-arg versionGo=${bamboo.ndsaatrmq.versionGo} --build-arg versionFluentd=${bamboo.ndsaatrmq.versionFluentd} --rm -t $reg_url/${bamboo.service_name}:${bamboo.ndsaatrmq.version} .\n\ndocker image ls --digests --format='Name : {{.Repository}}:{{.Tag}}. Size : {{.Size}}. Digest : {{.Digest}}' $reg_url/${bamboo.service_name}:${bamboo.ndsaatrmq.version}\n\ndocker push $reg_url/${bamboo.service_name}:${bamboo.ndsaatrmq.version}\n\ndocker rmi $reg_url/${bamboo.service_name}:${bamboo.ndsaatrmq.version}"))
//                                                                                 .dockerConfiguration(
//                                                                                                 new DockerConfiguration()
//                                                                                                                 .enabled(false))))

                                // .planRepositories(
                                //                 new GitRepository()
                                //                                 .name("Docker")
                                //                                 .url(gitUrl)
                                //                                 .branch(branch));

//         }

// }
