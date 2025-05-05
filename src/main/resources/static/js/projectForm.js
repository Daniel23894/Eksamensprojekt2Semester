document.addEventListener('DOMContentLoaded', function() {
    const projectForm = document.getElementById('project-form');

    if (projectForm) {
        projectForm.addEventListener('submit', function(event) {
            event.preventDefault();

            if (!validateForm()) {
                return;
            }

            const formData = {
                name: document.getElementById('project-name').value,
                description: document.getElementById('project-description').value,
                startDate: document.getElementById('project-start-date').value,
                endDate: document.getElementById('project-end-date').value || null
            };

            submitProject(formData);
        });
    }

    function validateForm() {
        let valid = true;
        const errorMessages = document.querySelectorAll('.error-message');

        errorMessages.forEach(el => el.textContent = '');

        const nameInput = document.getElementById('project-name');
        if (!nameInput.value.trim()) {
            document.getElementById('name-error').textContent = 'Project name is required';
            valid = false;
        }

        const startDateInput = document.getElementById('project-start-date');
        if (!startDateInput.value) {
            document.getElementById('start-date-error').textContent = 'Start date is required';
            valid = false;
        }

        const endDateInput = document.getElementById('project-end-date');
        if (endDateInput.value && startDateInput.value && new Date(endDateInput.value) < new Date(startDateInput.value)) {
            document.getElementById('end-date-error').textContent = 'End date must be after start date';
            valid = false;
        }

        return valid;
    }

    function submitProject(projectData) {
        fetch('/api/projects', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(projectData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    showNotification('success', data.message);

                    projectForm.reset();
                } else {
                    showNotification('error', data.message || 'Failed to create project');

                    if (data.data) {
                        Object.entries(data.data).forEach(([field, message]) => {
                            const errorElement = document.getElementById(`${field}-error`);
                            if (errorElement) {
                                errorElement.textContent = message;
                            }
                        });
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('error', 'An unexpected error occurred');
            });
    }

    function showNotification(type, message) {
        const notification = document.getElementById('notification');
        if (notification) {
            notification.textContent = message;
            notification.className = `notification ${type}`;
            notification.style.display = 'block';

            setTimeout(() => {
                notification.style.display = 'none';
            }, 5000);
        }
    }
});