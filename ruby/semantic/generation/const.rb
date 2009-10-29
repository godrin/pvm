class ConstCode
  attr_reader :name
  
  def before
    []
  end
  def after
    []
  end
  
  def value
    @name
  end
  def code
    []
  end
end