const API = "http://localhost:8080";

async function loadDashboard() {
    try {
        const donors = await fetch(`${API}/donors`).then(r => r.json());
        const donations = await fetch(`${API}/donations`).then(r => r.json());
        const bloodUnits = await fetch(`${API}/blood-units`).then(r => r.json());
        const issues = await fetch(`${API}/issues`).then(r => r.json());

        document.getElementById("donorCount").textContent = donors.length;
        document.getElementById("donationCount").textContent = donations.length;
        document.getElementById("bloodUnitCount").textContent = bloodUnits.length;
        document.getElementById("issueCount").textContent = issues.length;

    } catch (error) {
        console.error("Dashboard error:", error);
    }
}

async function loadDonors() {
    const response = await fetch(`${API}/donors`);
    const donors = await response.json();

    const area = document.getElementById("donorList");

    if (donors.length === 0) {
        area.innerHTML = `<p class="empty">No donors found.</p>`;
        return;
    }

    area.innerHTML = donors.map(donor => `
        <div>
            <p>
                <strong>${donor.name}</strong><br>
                Blood Group: ${donor.bloodGroup}
                &nbsp; | &nbsp;
                Age: ${donor.age}
                &nbsp; | &nbsp;
                Phone: ${donor.phone}
            </p>
        </div>
    `).join("");
}

async function loadBloodUnits() {
    const response = await fetch(`${API}/blood-units`);
    const units = await response.json();

    const area = document.getElementById("bloodUnitList");

    if (units.length === 0) {
        area.innerHTML = `<p class="empty">No blood units found.</p>`;
        return;
    }

    area.innerHTML = units.map(unit => `
        <div>
            <p>
                <strong>Unit #${unit.id}</strong>
                &nbsp; | &nbsp;
                Blood Group: ${unit.bloodGroup}
                &nbsp; | &nbsp;
                Units: ${unit.units}
                &nbsp; | &nbsp;
                Status: ${unit.status}
                &nbsp; | &nbsp;
                Expiry: ${unit.expiryDate}
            </p>
        </div>
    `).join("");
}

async function loadNearExpiry() {
    const response = await fetch(`${API}/blood-units/near-expiry`);
    const units = await response.json();

    const area = document.getElementById("nearExpiryList");

    if (units.length === 0) {
        area.innerHTML = `<p class="empty">No near-expiry blood units.</p>`;
        return;
    }

    area.innerHTML = units.map(unit => `
        <div>
            <p>
                <strong>⚠ Unit #${unit.id}</strong>
                &nbsp; | &nbsp;
                ${unit.bloodGroup}
                &nbsp; | &nbsp;
                ${unit.units} units
                &nbsp; | &nbsp;
                Expires: ${unit.expiryDate}
            </p>
        </div>
    `).join("");
}

async function loadIssues() {
    const response = await fetch(`${API}/issues`);
    const issues = await response.json();

    const area = document.getElementById("issueList");

    if (issues.length === 0) {
        area.innerHTML = `<p class="empty">No issue records found.</p>`;
        return;
    }

    area.innerHTML = issues.map(issue => `
        <div>
            <p>
                <strong>${issue.recipientName}</strong>
                &nbsp; | &nbsp;
                Units Issued: ${issue.unitsIssued}
                &nbsp; | &nbsp;
                Date: ${issue.issueDate}
            </p>
        </div>
    `).join("");
}

loadDashboard();