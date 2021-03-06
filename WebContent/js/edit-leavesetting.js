angular
		.module('edit-leavesetting', [ 'formly', 'formlyBootstrap' ])
		.constant('formlyExampleApiCheck', apiCheck())
		.config(
				function config(formlyConfigProvider, formlyExampleApiCheck) {
					// set templates here
					formlyConfigProvider
							.setType({
								name : 'matchField',
								apiCheck : function() {
									return {
										data : {
											fieldToMatch : formlyExampleApiCheck.string
										}
									}
								},
								apiCheckOptions : {
									prefix : 'matchField type'
								},
								defaultOptions : function matchFieldDefaultOptions(
										options) {
									return {
										extras : {
											validateOnModelChange : true
										},
										expressionProperties : {
											'templateOptions.disabled' : function(
													viewValue, modelValue,
													scope) {
												var matchField = find(
														scope.fields,
														'key',
														options.data.fieldToMatch);
												if (!matchField) {
													throw new Error(
															'Could not find a field for the key '
																	+ options.data.fieldToMatch);
												}
												var model = options.data.modelToMatch
														|| scope.model;
												var originalValue = model[options.data.fieldToMatch];
												var invalidOriginal = matchField.formControl
														&& matchField.formControl.$invalid;
												return !originalValue
														|| invalidOriginal;
											}
										},
										validators : {
											fieldMatch : {
												expression : function(
														viewValue, modelValue,
														fieldScope) {
													var value = modelValue
															|| viewValue;
													var model = options.data.modelToMatch
															|| fieldScope.model;
													return value === model[options.data.fieldToMatch];
												},
												message : options.data.matchFieldMessage
														|| '"Must match"'
											}
										}
									};

									function find(array, prop, value) {
										var foundItem;
										array.some(function(item) {
											if (item[prop] === value) {
												foundItem = item;
											}
											return !!foundItem;
										});
										return foundItem;
									}
								}
							});
				}).controller(
				'edit-leavesetting',
				function($rootScope, $scope, $stateParams, $state,
						formlyVersion, leavesetting, leavesettingService,
						recordtypeService) {
					var id = ($stateParams.id) ? parseInt($stateParams.id) : 0;
					$rootScope.title = (id > 0) ? 'Edit Leavesetting'
							: 'Add Leavesetting';
					$rootScope.buttonText = (id > 0) ? 'Update' : 'Add';
					$scope.types = [];
					var vm = this;
					$scope.vm = vm;
					vm.onSubmit = onSubmit;
					vm.author = {
						name : 'pohsun',
						url : ''
					};
					vm.env = {
						angularVersion : angular.version.full,
						formlyVersion : formlyVersion
					};

					vm.model = leavesetting.data;
					vm.confirmationModel = {};

					recordtypeService.list().then(function(status) {
						var deps = status.data.content;
						angular.forEach(deps, function(dep) {
							$scope.types.push({
								name : dep.name,
								value : dep.id
							});
						});
					});

					vm.fields = [ {
						key : 'name',
						type : 'input',
						templateOptions : {
							label : 'Name',
							placeholder : 'name',
							type : 'text',
							required : true
						}
					},
					{
						key : 'type',
						fieldGroup : [ {
							key : 'id',
							type : 'select',
							templateOptions : {
								required : true,
								label : 'Type',
								options : $scope.types
							}
						} ]
					}, {
						key : 'year',
						type : 'input',
						templateOptions : {
							label : 'year',
							placeholder : 'year',
							type : 'number',
							required : true
						}
					}, {
						key : 'days',
						type : 'input',
						templateOptions : {
							label : 'days',
							placeholder : 'days',
							type : 'number',
							required : true
						}
					} ];
					function onSubmit() {
						console.log("Id : " + id);
						if (id > 0) {
							console.log("Update");
							leavesettingService.update(id, vm.model).then(
									function(status) {
										$state.go('dashboard.list-leavesettings');
									});
						} else {
							console.log("Insert");
							leavesettingService.insert(vm.model).then(
									function(status) {
										$state.go('dashboard.list-leavesettings');
									});
						}
					}

				});
