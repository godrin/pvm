class IvarCode
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
    s(@name.to_s)
  end
end