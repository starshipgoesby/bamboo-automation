const express = require('express');
const { exec } = require('child_process');
const fs = require('fs');

const app = express();
const port = 3003;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static('public'));

// Endpoint to send the original Java code
app.get('/getOriginalJavaCode', (req, res) => {
    try {
        const originalCode = fs.readFileSync('src/main/java/com/my/company/PlanSpec.java', 'utf8');
        res.json({ originalCode });
    } catch (error) {
        console.error('Error reading original Java code:', error);
        res.status(500).send('Error reading original Java code.');
    }
});

// Endpoint to restore Java code
app.post('/restoreJavaCode', (req, res) => {
    const { originalCode } = req.body;
    try {
        fs.writeFileSync('src/main/java/com/my/company/PlanSpec.java', originalCode);
        res.send('Java code restored successfully!');
    } catch (error) {
        console.error('Error restoring Java code:', error);
        res.status(500).send('Error restoring Java code.');
    }
});

app.post('/update', async (req, res) => {
    const { projectName, projectKey, planName, planKey, gitUrl, branch } = req.body;

    // Read the existing Java code from PlanSpec.java
    let javaCode = fs.readFileSync('src/main/java/com/my/company/PlanSpec.java', 'utf8');


    // Store the original Java code
    const originalJavaCode = javaCode;

    // Replace placeholders with form input values in the Java code
    javaCode = javaCode.replace('${projectName}', projectName)
                       .replace('${projectKey}', projectKey)
                       .replace('${planName}', planName)
                       .replace('${planKey}', planKey)
                       .replace('${gitUrl}', gitUrl)
                       .replace('${branch}', branch);

    // Write the updated Java code back to PlanSpec.java
    fs.writeFileSync('src/main/java/com/my/company/PlanSpec.java', javaCode);

    // Execute mvn -Ppublish-specs command
    exec('mvn -Ppublish-specs -e', (error, stdout) => {
        if (error) {
            console.error(`Error executing command: ${error.message}`);
            console.error(`Command output: ${stdout}`);
            res.status(500).send('Error executing command.');
            return;
        }
        console.log(`Command output: ${stdout}`);
        // Restore the original Java code
        try {
            fs.writeFileSync('src/main/java/com/my/company/PlanSpec.java', originalJavaCode);
        } catch (restoreError) {
            console.error('Error restoring original Java code:', restoreError);
            res.status(500).send('Error restoring original Java code.');
            return;
        }
        res.send('Command executed successfully!');
    });
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
